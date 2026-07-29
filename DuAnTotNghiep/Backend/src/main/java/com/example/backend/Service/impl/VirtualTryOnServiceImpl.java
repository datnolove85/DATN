package com.example.backend.Service.impl;

import com.example.backend.Entity.HinhAnh;
import com.example.backend.Exception.VirtualTryOnException;
import com.example.backend.Repository.HinhAnhRepository;
import com.example.backend.Response.VirtualTryOnResult;
import com.example.backend.Service.VirtualTryOnService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class VirtualTryOnServiceImpl implements VirtualTryOnService {

    private static final long MAX_PERSON_IMAGE_BYTES = 10L * 1024L * 1024L;
    private static final List<String> ALLOWED_CATEGORIES = List.of(
            "upper_body",
            "lower_body",
            "dresses"
    );

    private final HinhAnhRepository hinhAnhRepository;
    private final JsonMapper jsonMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    @Value("${huggingface.token:}")
    private String huggingFaceToken;

    @Value("${huggingface.space-url:https://yisol-idm-vton.hf.space}")
    private String spaceUrl;

    @Value("${huggingface.tryon-endpoint:/tryon}")
    private String tryOnEndpoint;

    @Value("${huggingface.timeout-seconds:300}")
    private int timeoutSeconds;

    @Value("${app.product-upload-dir:uploads/sanpham}")
    private String productUploadDir;

    @Override
    public VirtualTryOnResult tryOn(
            Integer sanPhamChiTietId,
            MultipartFile personImage,
            String category
    ) {
        validateRequest(sanPhamChiTietId, personImage, category);

        try {
            BinaryImage human = new BinaryImage(
                    personImage.getBytes(),
                    safeFilename(personImage.getOriginalFilename(), "person.jpg"),
                    normalizeContentType(personImage.getContentType(), "image/jpeg")
            );

            HinhAnh garmentEntity = findGarmentImage(sanPhamChiTietId);
            BinaryImage garment = loadGarmentImage(garmentEntity);

            List<String> uploadedPaths = uploadImagesToGradio(human, garment);
            if (uploadedPaths.size() < 2) {
                throw new VirtualTryOnException("Hugging Face không trả đủ đường dẫn ảnh đã tải lên");
            }

            // 1. Submit yêu cầu và nhận về cả pathPrefix lẫn eventId
            SubmitResult submitResult = submitPrediction(
                    uploadedPaths.get(0),
                    human,
                    uploadedPaths.get(1),
                    garment,
                    category
            );

            // 2. Chờ kết quả bằng đúng pathPrefix đã dùng
            String resultUrl = waitForResult(submitResult.pathPrefix(), submitResult.eventId());
            return downloadResult(resultUrl);

        } catch (VirtualTryOnException e) {
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new VirtualTryOnException("Tiến trình thử đồ đã bị gián đoạn", e);
        } catch (Exception e) {
            throw new VirtualTryOnException("Không thể tạo ảnh thử đồ lúc này", e);
        }
    }
    private void validateRequest(
            Integer sanPhamChiTietId,
            MultipartFile personImage,
            String category
    ) {
        if (huggingFaceToken == null || huggingFaceToken.isBlank()) {
            throw new VirtualTryOnException(
                    "Backend chưa được cấu hình biến môi trường HF_TOKEN"
            );
        }

        if (sanPhamChiTietId == null || sanPhamChiTietId <= 0) {
            throw new VirtualTryOnException("ID sản phẩm chi tiết không hợp lệ");
        }

        if (personImage == null || personImage.isEmpty()) {
            throw new VirtualTryOnException("Bạn chưa chọn ảnh người thử");
        }

        String contentType = Optional.ofNullable(personImage.getContentType())
                .orElse("")
                .toLowerCase(Locale.ROOT);
        if (!contentType.startsWith("image/")) {
            throw new VirtualTryOnException("Tệp người thử phải là ảnh");
        }

        if (personImage.getSize() > MAX_PERSON_IMAGE_BYTES) {
            throw new VirtualTryOnException("Ảnh người thử không được vượt quá 10 MB");
        }

        if (!ALLOWED_CATEGORIES.contains(normalizeCategory(category))) {
            throw new VirtualTryOnException("Loại trang phục không hợp lệ");
        }
    }

    private HinhAnh findGarmentImage(Integer sanPhamChiTietId) {
        return hinhAnhRepository
                .findFirstByIdSanPhamChiTiet_IdAndLaAnhChinhTrueAndTrangThaiTrue(
                        sanPhamChiTietId
                )
                .or(() -> hinhAnhRepository
                        .findFirstByIdSanPhamChiTiet_IdAndTrangThaiTrueOrderByIdAsc(
                                sanPhamChiTietId
                        ))
                .orElseThrow(() -> new VirtualTryOnException(
                        "Sản phẩm chi tiết chưa có ảnh để thử đồ"
                ));
    }

    private BinaryImage loadGarmentImage(HinhAnh image) throws IOException, InterruptedException {
        String link = Optional.ofNullable(image.getLink())
                .orElse("")
                .trim();
        if (link.isBlank()) {
            throw new VirtualTryOnException("Đường dẫn ảnh sản phẩm đang trống");
        }

        if (link.startsWith("http://") || link.startsWith("https://")) {
            HttpRequest request = requestBuilder(URI.create(link), false)
                    .GET()
                    .timeout(Duration.ofSeconds(60))
                    .build();
            HttpResponse<byte[]> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofByteArray()
            );
            ensureSuccess(response.statusCode(), "Không tải được ảnh sản phẩm từ URL");
            return new BinaryImage(
                    response.body(),
                    safeFilename(image.getTenAnh(), "garment.jpg"),
                    response.headers().firstValue("Content-Type")
                            .orElse(normalizeContentType(image.getDinhDang(), "image/jpeg"))
            );
        }

        String relative = link.replace('\\', '/');
        if (relative.startsWith("/sanpham/")) {
            relative = relative.substring("/sanpham/".length());
        } else if (relative.startsWith("sanpham/")) {
            relative = relative.substring("sanpham/".length());
        }

        Path root = Paths.get(productUploadDir).toAbsolutePath().normalize();
        Path file = root.resolve(relative).normalize();
        if (!file.startsWith(root)) {
            throw new VirtualTryOnException("Đường dẫn ảnh sản phẩm không an toàn");
        }
        if (!Files.isRegularFile(file)) {
            throw new VirtualTryOnException(
                    "Không tìm thấy ảnh sản phẩm trên backend: " + file.getFileName()
            );
        }

        String detected = Files.probeContentType(file);
        return new BinaryImage(
                Files.readAllBytes(file),
                safeFilename(image.getTenAnh(), file.getFileName().toString()),
                normalizeContentType(detected, normalizeContentType(image.getDinhDang(), "image/jpeg"))
        );
    }

    private List<String> uploadImagesToGradio(
            BinaryImage human,
            BinaryImage garment
    ) throws IOException, InterruptedException {
        String boundary = "----DATNTryOn" + UUID.randomUUID();
        byte[] multipartBody = multipartBody(boundary, List.of(human, garment));

        // Thử endpoint chuẩn direct space trước
        String uploadUrl = baseUrl() + "/upload";
        HttpRequest request = authorizedRequest(URI.create(uploadUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                .timeout(Duration.ofSeconds(120))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        // Dự phòng nếu /upload bị 404 thì gọi /gradio_api/upload
        if (response.statusCode() == 404) {
            uploadUrl = baseUrl() + "/gradio_api/upload";
            request = authorizedRequest(URI.create(uploadUrl))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                    .timeout(Duration.ofSeconds(120))
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        }

        ensureSuccess(response.statusCode(), "Không tải được ảnh lên Hugging Face");

        JsonNode root = jsonMapper.readTree(response.body());
        if (!root.isArray()) {
            throw new VirtualTryOnException("Phản hồi upload của Hugging Face không hợp lệ");
        }

        java.util.ArrayList<String> paths = new java.util.ArrayList<>();
        for (int index = 0; index < root.size(); index++) {
            JsonNode value = root.get(index);
            if (value == null) continue;

            // Hỗ trợ cả Gradio cũ (String) và Gradio 4+ (Object)
            if (value.isTextual()) {
                paths.add(value.asText());
            } else if (value.isObject()) {
                if (value.has("path")) {
                    paths.add(value.get("path").asText());
                } else if (value.has("name")) {
                    paths.add(value.get("name").asText());
                }
            }
        }
        return paths;
    }
    private SubmitResult submitPrediction(
            String humanPath,
            BinaryImage human,
            String garmentPath,
            BinaryImage garment,
            String category
    ) throws IOException, InterruptedException {
        ObjectNode humanFile = fileData(humanPath, human);
        ObjectNode garmentFile = fileData(garmentPath, garment);

        ObjectNode editor = jsonMapper.createObjectNode();
        editor.set("background", humanFile);
        editor.set("layers", jsonMapper.createArrayNode());
        editor.putNull("composite");

        ArrayNode data = jsonMapper.createArrayNode();
        data.add(editor);
        data.add(garmentFile);
        data.add(garmentDescription(category));
        data.add(true);
        data.add(false);
        data.add(30);
        data.add(ThreadLocalRandom.current().nextInt(1, 1_000_000));

        ObjectNode payload = jsonMapper.createObjectNode();
        payload.set("data", data);

        String endpoint = normalizeEndpoint(tryOnEndpoint);

        // Thử dùng prefix /call trước
        String pathPrefix = "/call" + endpoint;
        String callUrl = baseUrl() + pathPrefix;

        HttpRequest request = authorizedRequest(URI.create(callUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(
                        jsonMapper.writeValueAsString(payload),
                        StandardCharsets.UTF_8
                ))
                .timeout(Duration.ofSeconds(60))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        // Nếu /call bị 404 thì chuyển sang /gradio_api/call
        if (response.statusCode() == 404) {
            pathPrefix = "/gradio_api/call" + endpoint;
            callUrl = baseUrl() + pathPrefix;
            request = authorizedRequest(URI.create(callUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            jsonMapper.writeValueAsString(payload),
                            StandardCharsets.UTF_8
                    ))
                    .timeout(Duration.ofSeconds(60))
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        }

        ensureSuccess(response.statusCode(), "Hugging Face từ chối yêu cầu thử đồ");

        String eventId = jsonMapper.readTree(response.body())
                .path("event_id")
                .asText("")
                .trim();
        if (eventId.isBlank()) {
            throw new VirtualTryOnException("Hugging Face không trả event_id");
        }

        // Trả về cả prefix thành công và eventId
        return new SubmitResult(pathPrefix, eventId);
    }

    private String waitForResult(String pathPrefix, String eventId) throws IOException, InterruptedException {
        String streamUrl = baseUrl() + pathPrefix + "/" + eventId;

        HttpRequest request = authorizedRequest(URI.create(streamUrl))
                .header("Accept", "text/event-stream")
                .GET()
                .timeout(Duration.ofSeconds(Math.max(timeoutSeconds, 60)))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        // Dự phòng: nếu tiền tố cũ vẫn 404, tự đổi tiền tố còn lại
        if (response.statusCode() == 404) {
            String altPrefix = pathPrefix.startsWith("/gradio_api")
                    ? pathPrefix.replace("/gradio_api", "")
                    : "/gradio_api" + pathPrefix;
            streamUrl = baseUrl() + altPrefix + "/" + eventId;

            request = authorizedRequest(URI.create(streamUrl))
                    .header("Accept", "text/event-stream")
                    .GET()
                    .timeout(Duration.ofSeconds(Math.max(timeoutSeconds, 60)))
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        }

        ensureSuccess(response.statusCode(), "Không lấy được kết quả thử đồ");

        String currentEvent = "";
        for (String rawLine : response.body().split("\\R")) {
            String line = rawLine.trim();
            if (line.startsWith("event:")) {
                currentEvent = line.substring("event:".length()).trim();
                continue;
            }
            if (!line.startsWith("data:")) {
                continue;
            }

            String data = line.substring("data:".length()).trim();
            if ("error".equalsIgnoreCase(currentEvent)) {
                throw new VirtualTryOnException(
                        "API thử đồ báo lỗi: " + truncate(data, 300)
                );
            }
            if ("complete".equalsIgnoreCase(currentEvent)) {
                JsonNode output = jsonMapper.readTree(data);
                String resultUrl = findResultUrl(output);
                if (resultUrl == null || resultUrl.isBlank()) {
                    throw new VirtualTryOnException("API không trả về ảnh kết quả");
                }
                return normalizeResultUrl(resultUrl);
            }
        }

        throw new VirtualTryOnException("API kết thúc nhưng không có sự kiện complete");
    }
    private VirtualTryOnResult downloadResult(String resultUrl)
            throws IOException, InterruptedException {
        URI resultUri = URI.create(resultUrl);
        HttpRequest request = requestBuilder(resultUri, isHuggingFaceHost(resultUri))
                .GET()
                .timeout(Duration.ofSeconds(120))
                .build();
        HttpResponse<byte[]> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );
        ensureSuccess(response.statusCode(), "Không tải được ảnh kết quả từ Hugging Face");

        String contentType = response.headers()
                .firstValue("Content-Type")
                .orElse("image/png");
        if (!contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            contentType = "image/png";
        }
        return new VirtualTryOnResult(response.body(), contentType);
    }

    private ObjectNode fileData(String uploadedPath, BinaryImage image) {
        ObjectNode node = jsonMapper.createObjectNode();
        node.put("path", uploadedPath);
        node.put("orig_name", image.filename());
        node.put("size", image.bytes().length);
        node.put("mime_type", image.contentType());

        ObjectNode meta = jsonMapper.createObjectNode();
        meta.put("_type", "gradio.FileData");
        node.set("meta", meta);
        return node;
    }

    private byte[] multipartBody(String boundary, List<BinaryImage> files) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (BinaryImage file : files) {
            writeUtf8(output, "--" + boundary + "\r\n");
            writeUtf8(
                    output,
                    "Content-Disposition: form-data; name=\"files\"; filename=\""
                            + escapeHeader(file.filename())
                            + "\"\r\n"
            );
            writeUtf8(output, "Content-Type: " + file.contentType() + "\r\n\r\n");
            output.write(file.bytes());
            writeUtf8(output, "\r\n");
        }
        writeUtf8(output, "--" + boundary + "--\r\n");
        return output.toByteArray();
    }

    private HttpRequest.Builder authorizedRequest(URI uri) {
        return requestBuilder(uri, true);
    }

    private HttpRequest.Builder requestBuilder(URI uri, boolean includeHuggingFaceToken) {
        HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                .header("User-Agent", "DATN-Virtual-Try-On/1.0");
        if (includeHuggingFaceToken) {
            builder.header("Authorization", "Bearer " + huggingFaceToken.trim());
        }
        return builder;
    }

    private boolean isHuggingFaceHost(URI uri) {
        String host = Optional.ofNullable(uri.getHost())
                .orElse("")
                .toLowerCase(Locale.ROOT);
        return host.equals("huggingface.co")
                || host.endsWith(".huggingface.co")
                || host.equals("hf.space")
                || host.endsWith(".hf.space");
    }

    private String findResultUrl(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isTextual()) {
            return node.asText();
        }
        if (node.isArray()) {
            for (JsonNode child : node) {
                String found = findResultUrl(child);
                if (found != null && !found.isBlank()) {
                    return found;
                }
            }
            return null;
        }
        if (node.isObject()) {
            for (String key : List.of("url", "path", "image", "value")) {
                JsonNode child = node.get(key);
                String found = findResultUrl(child);
                if (found != null && !found.isBlank()) {
                    return found;
                }
            }
            for (JsonNode child : node) {
                String found = findResultUrl(child);
                if (found != null && !found.isBlank()) {
                    return found;
                }
            }
        }
        return null;
    }

    private String normalizeResultUrl(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }
        if (trimmed.startsWith("/gradio_api/") || trimmed.startsWith("/file=")) {
            return baseUrl() + trimmed;
        }
        return baseUrl() + "/gradio_api/file=" + trimmed.replace(" ", "%20");
    }

    private String normalizeCategory(String category) {
        if (category == null || category.isBlank()) {
            return "upper_body";
        }
        return category.trim().toLowerCase(Locale.ROOT);
    }

    private String garmentDescription(String category) {
        return switch (normalizeCategory(category)) {
            case "lower_body" -> "pants trousers skirt lower body garment";
            case "dresses" -> "dress one-piece garment";
            default -> "shirt jacket top upper body garment";
        };
    }

    private String normalizeEndpoint(String endpoint) {
        String value = endpoint == null || endpoint.isBlank() ? "/tryon" : endpoint.trim();
        return value.startsWith("/") ? value : "/" + value;
    }

    private String baseUrl() {
        String value = spaceUrl == null ? "" : spaceUrl.trim();
        while (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String safeFilename(String filename, String fallback) {
        String value = filename == null || filename.isBlank() ? fallback : filename;
        value = value.replace('\\', '_').replace('/', '_').replace('"', '_');
        return value.length() > 120 ? value.substring(value.length() - 120) : value;
    }

    private String normalizeContentType(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        return value.split(";", 2)[0].trim();
    }

    private String escapeHeader(String value) {
        return value.replace("\r", "_").replace("\n", "_").replace("\"", "_");
    }

    private void writeUtf8(ByteArrayOutputStream output, String value) throws IOException {
        output.write(value.getBytes(StandardCharsets.UTF_8));
    }

    private void ensureSuccess(int statusCode, String message) {
        if (statusCode < 200 || statusCode >= 300) {
            if (statusCode == 401 || statusCode == 403) {
                throw new VirtualTryOnException(
                        message + ": HF_TOKEN không hợp lệ hoặc không có quyền truy cập"
                );
            }
            if (statusCode == 429) {
                throw new VirtualTryOnException(
                        message + ": Hugging Face đang giới hạn lượt gọi hoặc ZeroGPU hết quota"
                );
            }
            throw new VirtualTryOnException(message + " (HTTP " + statusCode + ")");
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength) + "...";
    }

    private record BinaryImage(
            byte[] bytes,
            String filename,
            String contentType
    ) {
    }
    private record SubmitResult(String pathPrefix, String eventId) {}
}

package com.example.backend.Service.impl;

import com.example.backend.Entity.HinhAnh;
import com.example.backend.Exception.VirtualTryOnException;
import com.example.backend.Repository.HinhAnhRepository;
import com.example.backend.Response.VirtualTryOnResult;
import com.example.backend.Service.VirtualTryOnService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class VirtualTryOnServiceImpl implements VirtualTryOnService {

    private static final long MAX_PERSON_IMAGE_BYTES = 10L * 1024L * 1024L;
    private static final List<String> ALLOWED_CATEGORIES = List.of("upper_body", "lower_body", "dresses");

    private final HinhAnhRepository hinhAnhRepository;
    private final ObjectMapper objectMapper; // Dùng Jackson tiêu chuẩn của Spring Boot

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
    public VirtualTryOnResult tryOn(Integer sanPhamChiTietId, MultipartFile personImage, String category) {
        validateRequest(sanPhamChiTietId, personImage, category);

        try {
            BinaryImage human = new BinaryImage(
                    personImage.getBytes(),
                    safeFilename(personImage.getOriginalFilename(), "person.jpg"),
                    normalizeContentType(personImage.getContentType(), "image/jpeg")
            );

            HinhAnh garmentEntity = findGarmentImage(sanPhamChiTietId);
            BinaryImage garment = loadGarmentImage(garmentEntity);

            // 1. Tải 2 ảnh lên Gradio Temp Storage
            List<String> uploadedPaths = uploadImagesToGradio(human, garment);
            if (uploadedPaths.size() < 2) {
                throw new VirtualTryOnException("Hugging Face không trả đủ đường dẫn ảnh tải lên");
            }

            // 2. Gửi lệnh Predict đến AI
            SubmitResult submitResult = submitPrediction(
                    uploadedPaths.get(0), human,
                    uploadedPaths.get(1), garment,
                    category
            );

            // 3. Chờ và lấy URL kết quả từ EventStream
            String resultUrl = waitForResult(submitResult.pathPrefix(), submitResult.eventId());

            // 4. Tải file ảnh kết quả về
            return downloadResult(resultUrl);

        } catch (VirtualTryOnException e) {
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new VirtualTryOnException("Tiến trình thử đồ bị ngắt kết nối", e);
        } catch (Exception e) {
            throw new VirtualTryOnException("Lỗi hệ thống khi xử lý thử đồ AI: " + e.getMessage(), e);
        }
    }

    private void validateRequest(Integer spctId, MultipartFile personImage, String category) {
        if (huggingFaceToken == null || huggingFaceToken.isBlank()) {
            throw new VirtualTryOnException("Chưa cấu hình HF_TOKEN trong application.properties");
        }
        if (spctId == null || spctId <= 0) {
            throw new VirtualTryOnException("ID sản phẩm chi tiết không hợp lệ");
        }
        if (personImage == null || personImage.isEmpty()) {
            throw new VirtualTryOnException("Bạn chưa tải ảnh người thử lên");
        }
        if (personImage.getSize() > MAX_PERSON_IMAGE_BYTES) {
            throw new VirtualTryOnException("Ảnh người thử dung lượng phải < 10MB");
        }
        if (!ALLOWED_CATEGORIES.contains(normalizeCategory(category))) {
            throw new VirtualTryOnException("Danh mục trang phục không hợp lệ");
        }
    }

    private HinhAnh findGarmentImage(Integer spctId) {
        return hinhAnhRepository
                .findFirstByIdSanPhamChiTiet_IdAndLaAnhChinhTrueAndTrangThaiTrue(spctId)
                .or(() -> hinhAnhRepository.findFirstByIdSanPhamChiTiet_IdAndTrangThaiTrueOrderByIdAsc(spctId))
                .orElseThrow(() -> new VirtualTryOnException("Sản phẩm chi tiết này chưa có ảnh"));
    }

    @Value("${python.remove-bg-url:http://127.0.0.1:8000/remove-bg}")
    private String removeBgUrl;

    // Hàm tải ảnh sản phẩm và gửi qua Python FastAPI để làm sạch nền + căn giữa
    private BinaryImage loadGarmentImage(HinhAnh image) throws IOException, InterruptedException {
        byte[] rawGarmentBytes;
        String filename = safeFilename(image.getTenAnh(), "garment.jpg");

        String link = Optional.ofNullable(image.getLink()).orElse("").trim();
        if (link.isBlank()) throw new VirtualTryOnException("Đường dẫn ảnh sản phẩm bị rỗng");

        if (link.startsWith("http://") || link.startsWith("https://")) {
            HttpRequest request = requestBuilder(URI.create(link), false).GET().timeout(Duration.ofSeconds(30)).build();
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            ensureSuccess(response.statusCode(), "Không thể tải ảnh sản phẩm từ URL");
            rawGarmentBytes = response.body();
        } else {
            String relative = link.replace('\\', '/').replaceAll("^/??sanpham/", "");
            Path root = Paths.get(productUploadDir).toAbsolutePath().normalize();
            Path file = root.resolve(relative).normalize();

            if (!Files.isRegularFile(file)) {
                throw new VirtualTryOnException("Không tìm thấy tệp ảnh sản phẩm ở backend: " + file.getFileName());
            }
            rawGarmentBytes = Files.readAllBytes(file);
        }

        // --- Gửi ảnh sang FastAPI để Tách Nền & Căn Giữa ---
        try {
            byte[] processedBytes = processGarmentWithFastApi(rawGarmentBytes, filename);
            return new BinaryImage(processedBytes, filename, "image/png");
        } catch (Exception e) {
            // Nếu Python Service gặp lỗi, fallback dùng lại ảnh gốc để ứng dụng không bị dừng
            return new BinaryImage(rawGarmentBytes, filename, "image/jpeg");
        }
    }

    // Hàm gọi API Python
    private byte[] processGarmentWithFastApi(byte[] imageBytes, String filename) throws IOException, InterruptedException {
        String boundary = "----FastApiBoundary" + UUID.randomUUID();
        BinaryImage img = new BinaryImage(imageBytes, filename, "image/jpeg");
        byte[] multipartBody = multipartBody(boundary, List.of(img));

        HttpRequest request = HttpRequest.newBuilder(URI.create(removeBgUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                .timeout(Duration.ofSeconds(15))
                .build();

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() == 200) {
            return response.body();
        }
        throw new IOException("FastAPI Remove-BG trả về mã lỗi: " + response.statusCode());
    }

    private List<String> uploadImagesToGradio(BinaryImage human, BinaryImage garment) throws IOException, InterruptedException {
        String boundary = "----DATNTryOn" + UUID.randomUUID();
        byte[] multipartBody = multipartBody(boundary, List.of(human, garment));

        String uploadUrl = baseUrl() + "/upload";
        HttpRequest request = authorizedRequest(URI.create(uploadUrl))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 404) {
            uploadUrl = baseUrl() + "/gradio_api/upload";
            request = authorizedRequest(URI.create(uploadUrl))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        }

        ensureSuccess(response.statusCode(), "Upload ảnh lên Hugging Face thất bại");

        JsonNode root = objectMapper.readTree(response.body());
        List<String> paths = new ArrayList<>();
        if (root.isArray()) {
            for (JsonNode node : root) {
                if (node.isTextual()) paths.add(node.asText());
                else if (node.has("path")) paths.add(node.get("path").asText());
                else if (node.has("name")) paths.add(node.get("name").asText());
            }
        }
        return paths;
    }

    private SubmitResult submitPrediction(String humanPath, BinaryImage human, String garmentPath, BinaryImage garment, String category) throws IOException, InterruptedException {
        ObjectNode humanFile = fileData(humanPath, human);
        ObjectNode garmentFile = fileData(garmentPath, garment);

        ObjectNode editor = objectMapper.createObjectNode();
        editor.set("background", humanFile);
        editor.set("layers", objectMapper.createArrayNode());
        editor.putNull("composite");

        ArrayNode data = objectMapper.createArrayNode();
        data.add(editor);
        data.add(garmentFile);
        data.add(garmentDescription(category));
        data.add(true);
        data.add(false);
        data.add(30);
        data.add(ThreadLocalRandom.current().nextInt(1, 1_000_000));

        ObjectNode payload = objectMapper.createObjectNode();
        payload.set("data", data);

        String pathPrefix = "/call" + normalizeEndpoint(tryOnEndpoint);
        HttpRequest request = authorizedRequest(URI.create(baseUrl() + pathPrefix))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 404) {
            pathPrefix = "/gradio_api/call" + normalizeEndpoint(tryOnEndpoint);
            request = authorizedRequest(URI.create(baseUrl() + pathPrefix))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload), StandardCharsets.UTF_8))
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        }

        ensureSuccess(response.statusCode(), "Gửi yêu cầu thử đồ tới AI thất bại");

        String eventId = objectMapper.readTree(response.body()).path("event_id").asText("").trim();
        if (eventId.isBlank()) throw new VirtualTryOnException("AI không trả lại Event ID");

        return new SubmitResult(pathPrefix, eventId);
    }

    private String waitForResult(String pathPrefix, String eventId) throws IOException, InterruptedException {
        String streamUrl = baseUrl() + pathPrefix + "/" + eventId;
        HttpRequest request = authorizedRequest(URI.create(streamUrl))
                .header("Accept", "text/event-stream")
                .GET()
                .timeout(Duration.ofSeconds(timeoutSeconds))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        ensureSuccess(response.statusCode(), "Không thể nhận kết quả từ AI Stream");

        String currentEvent = "";
        for (String rawLine : response.body().split("\\R")) {
            String line = rawLine.trim();
            if (line.startsWith("event:")) {
                currentEvent = line.substring(6).trim();
            } else if (line.startsWith("data:")) {
                String data = line.substring(5).trim();
                if ("error".equalsIgnoreCase(currentEvent)) {
                    throw new VirtualTryOnException("AI Báo Lỗi: " + data);
                }
                if ("complete".equalsIgnoreCase(currentEvent)) {
                    JsonNode output = objectMapper.readTree(data);
                    String resultUrl = findResultUrl(output);
                    if (resultUrl == null) throw new VirtualTryOnException("Không tìm thấy URL ảnh kết quả");
                    return normalizeResultUrl(resultUrl);
                }
            }
        }
        throw new VirtualTryOnException("Xử lý AI quá thời gian hoặc bị ngắt đột ngột");
    }

    private VirtualTryOnResult downloadResult(String resultUrl) throws IOException, InterruptedException {
        HttpRequest request = requestBuilder(URI.create(resultUrl), true).GET().build();
        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        ensureSuccess(response.statusCode(), "Không thể tải ảnh kết quả về Backend");
        return new VirtualTryOnResult(response.body(), "image/png");
    }

    // --- Helper Methods ---
    private ObjectNode fileData(String path, BinaryImage img) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("path", path);
        node.put("orig_name", img.filename());
        node.put("size", img.bytes().length);
        node.put("mime_type", img.contentType());
        ObjectNode meta = objectMapper.createObjectNode();
        meta.put("_type", "gradio.FileData");
        node.set("meta", meta);
        return node;
    }

    private byte[] multipartBody(String boundary, List<BinaryImage> files) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (BinaryImage file : files) {
            out.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(("Content-Disposition: form-data; name=\"files\"; filename=\"" + file.filename() + "\"\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(("Content-Type: " + file.contentType() + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
            out.write(file.bytes());
            out.write("\r\n".getBytes(StandardCharsets.UTF_8));
        }
        out.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        return out.toByteArray();
    }

    private HttpRequest.Builder authorizedRequest(URI uri) { return requestBuilder(uri, true); }
    private HttpRequest.Builder requestBuilder(URI uri, boolean auth) {
        HttpRequest.Builder b = HttpRequest.newBuilder(uri).header("User-Agent", "DATN-TryOn/1.0");
        if (auth && huggingFaceToken != null && !huggingFaceToken.isBlank()) {
            b.header("Authorization", "Bearer " + huggingFaceToken.trim());
        }
        return b;
    }

    private String findResultUrl(JsonNode node) {
        if (node == null) return null;
        if (node.isTextual()) return node.asText();
        if (node.isArray()) {
            for (JsonNode c : node) {
                String res = findResultUrl(c);
                if (res != null) return res;
            }
        } else if (node.isObject()) {
            for (String k : List.of("url", "path", "image", "value")) {
                if (node.has(k)) {
                    String res = findResultUrl(node.get(k));
                    if (res != null) return res;
                }
            }
        }
        return null;
    }

    private String normalizeResultUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) return url;
        if (url.startsWith("/gradio_api/")) return baseUrl() + url;
        return baseUrl() + "/gradio_api/file=" + url.replace(" ", "%20");
    }

    private String normalizeCategory(String cat) { return cat == null ? "upper_body" : cat.trim().toLowerCase(Locale.ROOT); }
    private String garmentDescription(String cat) {
        return switch (normalizeCategory(cat)) {
            case "lower_body" -> "pants trousers skirt lower body garment";
            case "dresses" -> "dress one-piece garment";
            default -> "shirt jacket top upper body garment";
        };
    }
    private String normalizeEndpoint(String ep) { return ep.startsWith("/") ? ep : "/" + ep; }
    private String baseUrl() { return spaceUrl.replaceAll("/+$", ""); }
    private String safeFilename(String name, String def) { return name == null || name.isBlank() ? def : name.replaceAll("[^a-zA-Z0-9._-]", "_"); }
    private String normalizeContentType(String type, String def) { return type == null ? def : type.split(";")[0].trim(); }
    private void ensureSuccess(int code, String msg) {
        if (code < 200 || code >= 300) throw new VirtualTryOnException(msg + " (Mã lỗi HTTP: " + code + ")");
    }

    private record BinaryImage(byte[] bytes, String filename, String contentType) {}
    private record SubmitResult(String pathPrefix, String eventId) {}
}
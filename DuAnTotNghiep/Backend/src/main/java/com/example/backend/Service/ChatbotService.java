package com.example.backend.Service;

import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Request.ChatbotHistoryItem;
import com.example.backend.Request.ChatbotMessageRequest;
import com.example.backend.Response.ChatbotMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private static final int CATALOG_LIMIT = 40;
    private final RestTemplate restTemplate;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-flash-latest}")
    private String model;

    @Transactional(readOnly = true)
    public ChatbotMessageResponse answer(ChatbotMessageRequest request) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Chatbot chua duoc cau hinh. Hay dat bien moi truong GEMINI_API_KEY roi khoi dong lai backend.");
        }

        Map<String, Object> payload = Map.of(
                "systemInstruction", Map.of("parts", List.of(Map.of("text", systemInstruction()))),
                "contents", contents(request),
                "generationConfig", Map.of("temperature", 0.35, "maxOutputTokens", 600)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey.trim());

        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/"
                + model.trim() + ":generateContent";

        try {
            Map<?, ?> response = restTemplate.exchange(endpoint, HttpMethod.POST,
                    new HttpEntity<>(payload, headers), Map.class).getBody();
            String answer = extractText(response);
            if (answer == null || answer.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "Gemini khong tra ve noi dung. Vui long thu lai sau it phut.");
            }
            return new ChatbotMessageResponse(answer.trim());
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == 401 || exception.getStatusCode().value() == 403) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "GEMINI_API_KEY khong hop le hoac chua duoc cap quyen truy cap Gemini API.");
            }
            if (exception.getStatusCode().value() == 404) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "Gemini model khong kha dung. Hay dat GEMINI_MODEL=gemini-flash-latest trong .env.");
            }
            if (exception.getStatusCode().value() == 429) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "Gemini dang qua tai hoac da het han muc. Vui long thu lai sau.");
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Khong the ket noi Gemini. Kiem tra GEMINI_API_KEY va model da cau hinh.");
        }
    }

    private List<Map<String, Object>> contents(ChatbotMessageRequest request) {
        List<Map<String, Object>> contents = new ArrayList<>();
        if (request.history() != null) {
            request.history().stream()
                    .filter(item -> isSupportedRole(item.role()))
                    .limit(12)
                    .forEach(item -> contents.add(content(normalizeRole(item.role()), item.text())));
        }
        contents.add(content("user", request.message().trim()));
        return contents;
    }

    private boolean isSupportedRole(String role) {
        return role != null && (role.equalsIgnoreCase("user") || role.equalsIgnoreCase("model")
                || role.equalsIgnoreCase("assistant"));
    }

    private String normalizeRole(String role) {
        return role.equalsIgnoreCase("user") ? "user" : "model";
    }

    private Map<String, Object> content(String role, String text) {
        return Map.of("role", role, "parts", List.of(Map.of("text", text.trim())));
    }

    private String systemInstruction() {
        return """
                Ban la tro ly mua sam cua StyleHub, website ban quan ao tai Viet Nam.
                Tra loi bang tieng Viet tu nhien, ngan gon, lich su va uu tien giup khach chon/mua san pham.
                Chi tra loi cac cau hoi lien quan den san pham, size, chat lieu, cach phoi do, dat hang,
                thanh toan, giao hang, doi tra va su dung website. Neu cau hoi ngoai pham vi, hay lich su
                huong nguoi dung ve chu de mua sam cua StyleHub.

                Du lieu catalog ben duoi la nguon su that duy nhat ve ten, bien the, gia va ton kho.
                Khong biet ra san pham, gia, voucher, chinh sach, thoi gian giao hay ton kho khac.
                Khi du lieu chinh sach chua co, hay noi ro rang can lien he nhan vien de xac nhan.
                Khong yeu cau khach chia se mat khau, OTP, so the hay thong tin thanh toan nhay cam.
                De tu van size chinh xac, hay hoi them chieu cao, can nang va so do neu khach chua cung cap.

                CATALOG HIEN CO:
                """ + catalogContext();
    }

    private String catalogContext() {
        NumberFormat currency = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        List<SanPhamChiTiet> variants = sanPhamChiTietRepository.findAllDangKinhDoanh();
        if (variants.isEmpty()) {
            return "Chua co san pham nao dang ban.";
        }

        StringBuilder result = new StringBuilder();
        variants.stream().limit(CATALOG_LIMIT).forEach(item -> {
            int inventory = Math.max(0, safe(item.getSoLuongTon()) - safe(item.getSoLuongTamGiu()));
            result.append("- ").append(item.getIdSanPham().getTenSanPham())
                    .append(" | ma ").append(item.getMaSanPhamChiTiet())
                    .append(" | ").append(item.getIdSanPham().getIdDanhMuc().getTenDanhMuc())
                    .append(" | thuong hieu ").append(item.getIdSanPham().getIdThuongHieu().getTenThuongHieu())
                    .append(" | chat lieu ").append(item.getIdSanPham().getIdChatLieu().getTenChatLieu())
                    .append(" | mau ").append(item.getIdMauSac().getTenMauSac())
                    .append(" | size ").append(item.getIdKichThuoc().getTenKichThuoc())
                    .append(" | gia ").append(formatPrice(currency, item.getGiaBan()))
                    .append(" VND | con ").append(inventory).append(" san pham.\n");
        });
        return result.toString();
    }

    private int safe(Integer value) {
        return value == null ? 0 : value;
    }

    private String formatPrice(NumberFormat currency, BigDecimal value) {
        return currency.format(value == null ? BigDecimal.ZERO : value);
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map<?, ?> response) {
        if (response == null || !(response.get("candidates") instanceof List<?> candidates) || candidates.isEmpty()) {
            return null;
        }
        Object first = candidates.get(0);
        if (!(first instanceof Map<?, ?> candidate) || !(candidate.get("content") instanceof Map<?, ?> content)
                || !(content.get("parts") instanceof List<?> parts) || parts.isEmpty()) {
            return null;
        }
        Object part = parts.get(0);
        if (!(part instanceof Map<?, ?> textPart)) {
            return null;
        }
        Object text = textPart.get("text");
        return text instanceof String value ? value : null;
    }
}
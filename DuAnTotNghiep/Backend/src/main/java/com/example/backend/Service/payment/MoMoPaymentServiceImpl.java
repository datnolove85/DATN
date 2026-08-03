//package com.example.backend.Service.payment;
//
//import com.example.backend.Entity.HoaDon;
//import com.example.backend.Entity.PhuongThucThanhToan;
//import com.example.backend.Entity.ThanhToan;
//import com.example.backend.Repository.HoaDonRepository;
//import com.example.backend.Repository.PhuongThucThanhToanRepository;
//import com.example.backend.Repository.ThanhToanRepository;
//import com.example.backend.Request.PaymentRequest;
//import com.example.backend.Response.PaymentResponse;
//import com.example.backend.utils.MoMoEncoder;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.RestTemplate;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class MoMoPaymentServiceImpl implements PaymentService {
//
//    private final HoaDonRepository hoaDonRepository;
//    private final ThanhToanRepository thanhToanRepository;
//    private final PhuongThucThanhToanRepository phuongThucRepository;
//
//    @Value("${momo.partnerCode}")
//    private String partnerCode;
//
//    @Value("${momo.accessKey}")
//    private String accessKey;
//
//    @Value("${momo.secretKey}")
//    private String secretKey;
//
//    @Value("${momo.payUrl}")
//    private String momoPayUrl;
//
//    @Value("${momo.redirectUrl}")
//    private String redirectUrl;
//
//    @Value("${momo.ipnUrl}")
//    private String ipnUrl;
//
//    @Override
//    public String getCode() {
//        return "MOMO";
//    }
//
//    @Override
//    @Transactional
//    public PaymentResponse pay(PaymentRequest request) {
//        try {
//            HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
//
//            PhuongThucThanhToan pttt = phuongThucRepository
//                    .findByMaPhuongThuc("MOMO")
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phương thức MOMO"));
//
//            // 1. Lưu bản ghi ThanhToan
//            ThanhToan thanhToan = new ThanhToan();
//            thanhToan.setIdHoaDon(hoaDon);
//            thanhToan.setIdPhuongThucThanhToan(pttt);
//            thanhToan.setSoTien(hoaDon.getTongThanhToan());
//            thanhToan.setTrangThai("cho_thanh_toan");
//            thanhToan.setNgayThanhToan(LocalDateTime.now());
//            thanhToanRepository.save(thanhToan);
//
//            hoaDon.setTrangThaiThanhToan("chua_thanh_toan");
//            hoaDon.setNgayCapNhat(LocalDateTime.now());
//            hoaDonRepository.save(hoaDon);
//
//            // 2. Chuẩn bị giá trị dạng String
//            String currentPartnerCode = partnerCode.trim();
//            String currentAccessKey = accessKey.trim();
//            String currentSecretKey = secretKey.trim();
//            String currentRedirectUrl = redirectUrl.trim();
//            String currentIpnUrl = ipnUrl.trim();
//
//            String orderId = hoaDon.getId() + "_" + System.currentTimeMillis();
//            String requestId = String.valueOf(System.currentTimeMillis());
//
//            // Chuyển số tiền về long tuyệt đối không bị dính số thập phân .0
//            long amountLong = hoaDon.getTongThanhToan().longValue();
//            String amount = String.valueOf(amountLong);
//
//            // Đảm bảo orderInfo khớp hoàn toàn với chuỗi gốc
//            String orderInfo = "ThanhToanDonHang" + hoaDon.getId();
//            String requestType = "captureWallet";
//            String extraData = "";
//
//            // 3. Chuỗi băm Signature đúng chuẩn
//            String rawSignature = "accessKey=" + currentAccessKey +
//                    "&amount=" + amount +
//                    "&extraData=" + extraData +
//                    "&ipnUrl=" + currentIpnUrl +
//                    "&orderId=" + orderId +
//                    "&orderInfo=" + orderInfo +
//                    "&partnerCode=" + currentPartnerCode +
//                    "&redirectUrl=" + currentRedirectUrl +
//                    "&requestId=" + requestId +
//                    "&requestType=" + requestType;
//
//            // In ra Console máy tính để kiểm tra trực tiếp
//            System.out.println("=== RAW SIGNATURE ===");
//            System.out.println(rawSignature);
//
//            String signature = MoMoEncoder.hmacSha256(rawSignature, currentSecretKey);
//
//            // 4. JSON Body (Dùng LinkedHashMap để giữ thứ tự field)
//            Map<String, Object> body = new java.util.LinkedHashMap<>();
//            body.put("partnerCode", currentPartnerCode);
//            body.put("partnerName", "Test");
//            body.put("storeId", "MomoStore");
//            body.put("requestId", requestId);
//            body.put("amount", amountLong); // Gửi Long
//            body.put("orderId", orderId);
//            body.put("orderInfo", orderInfo);
//            body.put("redirectUrl", currentRedirectUrl);
//            body.put("ipnUrl", currentIpnUrl);
//            body.put("lang", "vi");
//            body.put("extraData", extraData);
//            body.put("requestType", requestType);
//            body.put("signature", signature);
//
//            // 5. Gửi Request
//            RestTemplate restTemplate = new RestTemplate();
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
//            ResponseEntity<Map> response = restTemplate.postForEntity(momoPayUrl.trim(), entity, Map.class);
//
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                Map<String, Object> responseBody = response.getBody();
//                Object resultCode = responseBody.get("resultCode");
//
//                if (resultCode != null && ("0".equals(String.valueOf(resultCode)) || 0 == ((Number) resultCode).intValue())) {
//                    String payUrl = (String) responseBody.get("payUrl");
//                    return new PaymentResponse(true, "Tạo link MoMo thành công", payUrl);
//                } else {
//                    String message = (String) responseBody.get("message");
//                    return new PaymentResponse(false, "Lỗi MoMo (" + resultCode + "): " + message, null);
//                }
//            }
//
//            return new PaymentResponse(false, "Không kết nối được MoMo Sandbox", null);
//
//        } catch (Exception e) {
//            return new PaymentResponse(false, "Lỗi tạo link MoMo: " + e.getMessage(), null);
//        }
//    }
//}
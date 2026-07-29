package com.example.backend.Service.payment;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.PhuongThucThanhToan;
import com.example.backend.Entity.ThanhToan;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Repository.PhuongThucThanhToanRepository;
import com.example.backend.Repository.ThanhToanRepository;
import com.example.backend.Request.PaymentRequest;
import com.example.backend.Response.PaymentResponse;
import com.example.backend.utils.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayPaymentServiceImpl implements PaymentService {

    private final HoaDonRepository hoaDonRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final PhuongThucThanhToanRepository phuongThucRepository;

    @Value("${vnp.tmnCode}")
    private String tmnCode;

    @Value("${vnp.hashSecret}")
    private String hashSecret;

    @Value("${vnp.payUrl}")
    private String vnpPayUrl;

    @Value("${vnp.returnUrl}")
    private String returnUrl;

    @Override
    public String getCode() {
        return "VNPAY";
    }

    @Override
    @Transactional
    public PaymentResponse pay(PaymentRequest request) {
        try {
            HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

            PhuongThucThanhToan pttt = phuongThucRepository
                    .findByMaPhuongThuc("VNPAY")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy phương thức VNPAY trong CSDL"));

            // Lấy ID hóa đơn làm mã giao dịch luôn để xíu nữa VNPay trả về còn biết đường update
            String vnp_TxnRef = String.valueOf(hoaDon.getId());

            // 1. Tạo bản ghi ThanhToan với trạng thái chờ thanh toán
            ThanhToan thanhToan = new ThanhToan();
            thanhToan.setIdHoaDon(hoaDon);
            thanhToan.setIdPhuongThucThanhToan(pttt);
            thanhToan.setSoTien(hoaDon.getTongThanhToan());
            thanhToan.setTrangThai("cho_thanh_toan");
            thanhToan.setNgayThanhToan(LocalDateTime.now());
            thanhToanRepository.save(thanhToan);

            // 2. Cập nhật trạng thái hóa đơn
            hoaDon.setTrangThaiThanhToan("chua_thanh_toan");
            hoaDon.setNgayCapNhat(LocalDateTime.now());
            hoaDonRepository.save(hoaDon);

            // 3. Gom tham số tạo URL VNPay
            long amount = hoaDon.getTongThanhToan().longValue() * 100; // VNPay yêu cầu nhân 100
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String vnp_IpAddr = "127.0.0.1";

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", tmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", request.getOrderInfo() != null ? request.getOrderInfo() : "Thanh toan don hang: " + hoaDon.getId());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", returnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

            cld.add(Calendar.MINUTE, 15);
            vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

            // Sắp xếp tham số theo alphabet để tạo mã băm SecureHash
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    hashData.append('&');
                    query.append('&');
                }
            }
            hashData.deleteCharAt(hashData.length() - 1);
            query.deleteCharAt(query.length() - 1);

            String vnp_SecureHash = VNPayUtil.hmacSHA512(hashSecret, hashData.toString());
            String paymentUrl = vnpPayUrl + "?" + query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;

            // Đồng bộ kiểu trả về với CODPaymentService và BankPaymentService (boolean, message, url)
            return new PaymentResponse(true, "Tạo link VNPay thành công", paymentUrl);
        } catch (Exception e) {
            return new PaymentResponse(false, "Lỗi tạo link VNPay: " + e.getMessage(), null);
        }
    }
}
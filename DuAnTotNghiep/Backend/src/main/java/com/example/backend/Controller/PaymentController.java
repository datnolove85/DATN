package com.example.backend.Controller;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.ThanhToan;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Repository.ThanhToanRepository;
import com.example.backend.Request.PaymentRequest;
import com.example.backend.Response.PaymentResponse;

import com.example.backend.Service.HoaDonService;
import com.example.backend.Service.payment.PaymentFactory;
import com.example.backend.Service.payment.PaymentService;
import com.example.backend.Service.payment.VoucherConsumeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentFactory paymentFactory;

    private final HoaDonRepository hoaDonRepository;

    private final ThanhToanRepository thanhToanRepository;

    private final VoucherConsumeService voucherConsumeService; // thêm dòng này

    private final HoaDonService hoaDonService;

    @PostMapping("/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest request) {

        return paymentFactory
                .get(request.getMethod())
                .pay(request);

    }

    @GetMapping("/vnpay-return")
    public void vnpayReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef"); // Chính là idHoaDon mà ta đã truyền vào ở trên!
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo"); // Mã giao dịch tại VNPAY
        String vnp_BankCode = request.getParameter("vnp_BankCode");           // Ngân hàng khách dùng (VD: NCB)
        boolean isSuccess = "00".equals(vnp_ResponseCode);

        // Nếu thanh toán thành công từ VNPay trả về
        if (isSuccess && vnp_TxnRef != null) {
            try {
                Integer idHoaDon = Integer.valueOf(vnp_TxnRef);

                // 1. Tìm hóa đơn trong CSDL
                HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElse(null);
                if (hoaDon != null) {
                    // Cập nhật trạng thái Hóa đơn
                    hoaDonService.thayDoiTrangThai(
                            hoaDon.getId(),
                            "da_xac_nhan",
                            "Thanh toán VNPay thành công",
                            "CUSTOMER",
                            null
                    );
                    hoaDon.setTrangThaiThanhToan("da_thanh_toan");
                    hoaDon.setNgayCapNhat(LocalDateTime.now());
                    hoaDonRepository.save(hoaDon);
                    voucherConsumeService.consumeVoucher(hoaDon.getId());

                    // Cập nhật bảng ThanhToan sang "da_thanh_toan"
                    List<ThanhToan> listThanhToan = thanhToanRepository.getDanhSachThanhToanTheoHoaDon(hoaDon.getId());
                    for (ThanhToan tt : listThanhToan) {
                        if ("cho_thanh_toan".equals(tt.getTrangThai())) {
                            tt.setTrangThai("da_thanh_toan");
                            tt.setMaGiaoDich(vnp_TransactionNo);
                            thanhToanRepository.save(tt);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Lỗi cập nhật CSDL tự động từ vnpay-return: " + e.getMessage());
            }
        }

        // Điều hướng (Redirect) người dùng về trang kết quả ở Frontend
        String frontendUrl = isSuccess
                ? "http://localhost:5173/payment-result?status=success"
                : "http://localhost:5173/payment-result?status=failed";

        response.sendRedirect(frontendUrl);
    }

    // API nhận xác nhận kết quả thanh toán từ Frontend hoặc từ cổng trả về để chốt đơn vào DB
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentRequest request) {
        try {
            // 1. Tìm hóa đơn
            HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

            // 2. Cập nhật trạng thái Hóa đơn thành đã thanh toán / đã xác nhận
            hoaDonService.thayDoiTrangThai(
                    hoaDon.getId(),
                    "da_xac_nhan",
                    "Thanh toán VNPay thành công",
                    "CUSTOMER",
                    null
            );
            hoaDon.setTrangThaiThanhToan("da_thanh_toan");
            hoaDon.setNgayCapNhat(LocalDateTime.now());
            hoaDonRepository.save(hoaDon);
            voucherConsumeService.consumeVoucher(hoaDon.getId());

            // 3. Cập nhật bản ghi ThanhToan tương ứng sang "da_thanh_toan"
            List<ThanhToan> listThanhToan = thanhToanRepository.getDanhSachThanhToanTheoHoaDon(hoaDon.getId());
            for (ThanhToan tt : listThanhToan) {
                if ("cho_thanh_toan".equals(tt.getTrangThai())) {
                    tt.setTrangThai("da_thanh_toan");
                    thanhToanRepository.save(tt);
                }
            }

            return ResponseEntity.ok(new PaymentResponse(true, "Xác nhận thanh toán thành công", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new PaymentResponse(false, e.getMessage(), null));
        }
    }

}
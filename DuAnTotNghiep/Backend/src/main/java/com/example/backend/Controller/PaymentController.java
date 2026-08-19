package com.example.backend.Controller;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.PhuongThucThanhToan;
import com.example.backend.Entity.ThanhToan;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Repository.PhuongThucThanhToanRepository;
import com.example.backend.Repository.ThanhToanRepository;
import com.example.backend.Request.PaymentRequest;
import com.example.backend.Response.PaymentResponse;
import com.example.backend.Service.HoaDonService;
import com.example.backend.Service.payment.PaymentFactory;
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

    private final PhuongThucThanhToanRepository phuongThucRepository;

    private final VoucherConsumeService voucherConsumeService;

    private final HoaDonService hoaDonService;


    // ============================================================
    // 1. TẠO THANH TOÁN
    // ============================================================

    @PostMapping("/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest request) {

        return paymentFactory
                .get(request.getMethod())
                .pay(request);
    }


    // ============================================================
    // 2. VNPAY RETURN
    // ============================================================

    @GetMapping("/vnpay-return")
    public void vnpayReturn(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        // --------------------------------------------------------
        // Lấy thông tin VNPay trả về
        // --------------------------------------------------------

        String vnpResponseCode =
                request.getParameter("vnp_ResponseCode");

        String vnpTransactionStatus =
                request.getParameter("vnp_TransactionStatus");

        String vnpTxnRef =
                request.getParameter("vnp_TxnRef");

        String vnpTransactionNo =
                request.getParameter("vnp_TransactionNo");

        String vnpBankCode =
                request.getParameter("vnp_BankCode");


        System.out.println("========== VNPAY RETURN ==========");
        System.out.println("ResponseCode       = " + vnpResponseCode);
        System.out.println("TransactionStatus  = " + vnpTransactionStatus);
        System.out.println("TxnRef             = " + vnpTxnRef);
        System.out.println("TransactionNo      = " + vnpTransactionNo);
        System.out.println("BankCode            = " + vnpBankCode);
        System.out.println("==================================");


        // ========================================================
        // 1. KIỂM TRA KHÁCH HỦY THANH TOÁN
        // ========================================================

        if ("24".equals(vnpResponseCode)) {

            System.out.println(
                    "Khách hàng đã hủy thanh toán VNPay"
            );

            /*
             * QUAN TRỌNG:
             *
             * Không:
             * - Tạo ThanhToan
             * - Update ThanhToan
             * - set da_thanh_toan
             * - consume voucher
             *
             * Vì lúc tạo link VNPay chúng ta cũng chưa tạo
             * ThanhToan.
             */

            response.sendRedirect(
                    "http://localhost:5173/payment-result?status=cancel"
            );

            return;
        }


        // ========================================================
        // 2. KIỂM TRA THANH TOÁN THẤT BẠI
        // ========================================================

        boolean isSuccess =
                "00".equals(vnpResponseCode)
                        && "00".equals(vnpTransactionStatus);


        if (!isSuccess) {

            System.out.println(
                    "Thanh toán VNPay thất bại. "
                            + "ResponseCode = "
                            + vnpResponseCode
                            + ", TransactionStatus = "
                            + vnpTransactionStatus
            );

            /*
             * Không tạo ThanhToan
             * Không cập nhật ThanhToan
             * Không consume voucher
             */

            response.sendRedirect(
                    "http://localhost:5173/payment-result?status=failed"
            );

            return;
        }


        // ========================================================
        // 3. THANH TOÁN THÀNH CÔNG
        // ========================================================

        if (vnpTxnRef == null || vnpTxnRef.isBlank()) {

            System.err.println(
                    "VNPay trả về nhưng không có vnp_TxnRef"
            );

            response.sendRedirect(
                    "http://localhost:5173/payment-result?status=failed"
            );

            return;
        }


        try {

            Integer idHoaDon =
                    Integer.valueOf(vnpTxnRef);


            // ====================================================
            // 4. TÌM HÓA ĐƠN
            // ====================================================

            HoaDon hoaDon =
                    hoaDonRepository
                            .findById(idHoaDon)
                            .orElse(null);


            if (hoaDon == null) {

                System.err.println(
                        "Không tìm thấy hóa đơn ID = "
                                + idHoaDon
                );

                response.sendRedirect(
                        "http://localhost:5173/payment-result?status=failed"
                );

                return;
            }


            // ====================================================
            // 5. KIỂM TRA ĐÃ THANH TOÁN TRƯỚC ĐÓ CHƯA
            // ====================================================

            if ("da_thanh_toan".equals(
                    hoaDon.getTrangThaiThanhToan()
            )) {

                System.out.println(
                        "Hóa đơn "
                                + idHoaDon
                                + " đã được thanh toán trước đó."
                );

                response.sendRedirect(
                        "http://localhost:5173/payment-result?status=success"
                );

                return;
            }


            // ====================================================
            // 6. CẬP NHẬT TRẠNG THÁI HÓA ĐƠN
            // ====================================================

            hoaDonService.thayDoiTrangThai(
                    hoaDon.getId(),
                    "da_xac_nhan",
                    "Thanh toán VNPay thành công",
                    "CUSTOMER",
                    null
            );


            hoaDon.setTrangThaiThanhToan(
                    "da_thanh_toan"
            );

            hoaDon.setNgayCapNhat(
                    LocalDateTime.now()
            );

            hoaDonRepository.save(hoaDon);


            // ====================================================
            // 7. TÌM PHƯƠNG THỨC THANH TOÁN VNPAY
            // ====================================================

            PhuongThucThanhToan pttt =
                    phuongThucRepository
                            .findByMaPhuongThuc("VNPAY")
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Không tìm thấy phương thức VNPAY trong CSDL"
                                    )
                            );


            // ====================================================
            // 8. KIỂM TRA THANH TOÁN ĐÃ TỒN TẠI CHƯA
            // ====================================================

            List<ThanhToan> danhSachThanhToan =
                    thanhToanRepository
                            .getDanhSachThanhToanTheoHoaDon(
                                    hoaDon.getId()
                            );


            boolean daCoThanhToan =
                    danhSachThanhToan
                            .stream()
                            .anyMatch(tt ->
                                    "da_thanh_toan".equals(
                                            tt.getTrangThai()
                                    )
                            );


            // ====================================================
            // 9. CHỈ TẠO THANH TOÁN KHI VNPAY THÀNH CÔNG
            // ====================================================

            if (!daCoThanhToan) {

                ThanhToan thanhToan =
                        new ThanhToan();

                thanhToan.setIdHoaDon(
                        hoaDon
                );

                thanhToan.setIdPhuongThucThanhToan(
                        pttt
                );

                thanhToan.setSoTien(
                        hoaDon.getTongThanhToan()
                );

                thanhToan.setTrangThai(
                        "da_thanh_toan"
                );

                thanhToan.setMaGiaoDich(
                        vnpTransactionNo
                );

                thanhToan.setNgayThanhToan(
                        LocalDateTime.now()
                );

                thanhToanRepository.save(
                        thanhToan
                );


                System.out.println(
                        "Đã tạo ThanhToan thành công "
                                + "cho hóa đơn "
                                + hoaDon.getId()
                );
            }


            // ====================================================
            // 10. CONSUME VOUCHER
            // ====================================================

            voucherConsumeService.consumeVoucher(
                    hoaDon.getId()
            );


            // ====================================================
            // 11. REDIRECT VỀ FRONTEND - THÀNH CÔNG
            // ====================================================

            response.sendRedirect(
                    "http://localhost:5173/payment-result?status=success"
            );

        } catch (NumberFormatException e) {

            System.err.println(
                    "vnp_TxnRef không hợp lệ: "
                            + vnpTxnRef
            );

            response.sendRedirect(
                    "http://localhost:5173/payment-result?status=failed"
            );

        } catch (Exception e) {

            System.err.println(
                    "Lỗi xử lý VNPay return: "
                            + e.getMessage()
            );

            e.printStackTrace();

            response.sendRedirect(
                    "http://localhost:5173/payment-result?status=failed"
            );
        }
    }


    // ============================================================
    // 3. VERIFY PAYMENT
    // ============================================================
    //
    // Không dùng API này cho VNPay nữa.
    //
    // Khi VNPay thanh toán thành công:
    // /vnpay-return sẽ tự động xử lý.
    //
    // Khi VNPay hủy:
    // /vnpay-return sẽ không tạo ThanhToan.
    // ============================================================

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(
            @RequestBody PaymentRequest request
    ) {

        return ResponseEntity.badRequest()
                .body(
                        new PaymentResponse(
                                false,
                                "Không cần gọi verify-payment đối với VNPay. "
                                        + "Hệ thống tự động xác nhận khi VNPay "
                                        + "trả về kết quả thanh toán thành công.",
                                null
                        )
                );
    }
}
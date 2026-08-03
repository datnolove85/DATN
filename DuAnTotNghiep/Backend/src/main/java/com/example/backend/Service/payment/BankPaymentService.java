package com.example.backend.Service.payment;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.PhuongThucThanhToan;
import com.example.backend.Entity.ThanhToan;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Repository.PhuongThucThanhToanRepository;
import com.example.backend.Repository.ThanhToanRepository;
import com.example.backend.Request.PaymentRequest;
import com.example.backend.Response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class BankPaymentService implements PaymentService {

    private final HoaDonRepository hoaDonRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final PhuongThucThanhToanRepository phuongThucRepository;

    // thêm service này
    private final VoucherConsumeService voucherConsumeService;

    @Override
    public String getCode() {
        return "BANK";
    }

    @Override
    @Transactional
    public PaymentResponse pay(PaymentRequest request) {

        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        PhuongThucThanhToan pttt = phuongThucRepository
                .findByMaPhuongThuc("BANK")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương thức BANK"));

        // 1. Lưu lịch sử thanh toán
        ThanhToan thanhToan = new ThanhToan();
        thanhToan.setIdHoaDon(hoaDon);
        thanhToan.setIdPhuongThucThanhToan(pttt);
        thanhToan.setSoTien(hoaDon.getTongThanhToan());
        thanhToan.setTrangThai("da_thanh_toan");
        thanhToan.setNgayThanhToan(LocalDateTime.now());
        thanhToanRepository.save(thanhToan);

        // 2. Cập nhật hóa đơn
        hoaDon.setTrangThaiThanhToan("da_thanh_toan");
        hoaDon.setTrangThai("da_xac_nhan");
        hoaDon.setNgayCapNhat(LocalDateTime.now());
        hoaDonRepository.save(hoaDon);

        // ⭐ Trừ voucher tại đây
        voucherConsumeService.consumeVoucher(hoaDon.getId());

        String paymentUrl =
                "http://localhost:5173/payment/fake?paymentId=" + thanhToan.getId();

        return new PaymentResponse(
                true,
                "Thanh toán chuyển khoản thành công",
                paymentUrl
        );
    }
}
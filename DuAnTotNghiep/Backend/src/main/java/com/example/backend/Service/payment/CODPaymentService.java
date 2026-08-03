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
public class CODPaymentService implements PaymentService {

    private final HoaDonRepository hoaDonRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final PhuongThucThanhToanRepository phuongThucRepository;
    private final VoucherConsumeService voucherConsumeService;

    @Override
    public String getCode() {
        return "COD";
    }

    @Override
    @Transactional
    public PaymentResponse pay(PaymentRequest request) {

        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        PhuongThucThanhToan pttt = phuongThucRepository
                .findByMaPhuongThuc("COD")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương thức thanh toán COD"));

        // 1. Tạo lịch sử thanh toán
        ThanhToan thanhToan = new ThanhToan();
        thanhToan.setIdHoaDon(hoaDon);
        thanhToan.setIdPhuongThucThanhToan(pttt);
        thanhToan.setSoTien(hoaDon.getTongThanhToan());
        thanhToan.setTrangThai("cho_thanh_toan");
        thanhToan.setNgayThanhToan(LocalDateTime.now());

        thanhToanRepository.save(thanhToan);

        // 2. Cập nhật hóa đơn
        hoaDon.setTrangThaiThanhToan("chua_thanh_toan");
        hoaDon.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepository.save(hoaDon);

        // 3. Trừ voucher
        voucherConsumeService.consumeVoucher(hoaDon.getId());

        return new PaymentResponse(
                true,
                "Đơn hàng sẽ thanh toán khi nhận hàng",
                null
        );
    }
}
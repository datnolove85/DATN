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

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CODPaymentService implements PaymentService {

    private final HoaDonRepository hoaDonRepository;

    private final ThanhToanRepository thanhToanRepository;

    private final PhuongThucThanhToanRepository phuongThucRepository;

    @Override
    public String getCode() {
        return "COD";
    }

    @Override
    public PaymentResponse pay(PaymentRequest request) {

        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        PhuongThucThanhToan pttt = phuongThucRepository
                .findByMaPhuongThuc("COD")
                .orElseThrow();

        ThanhToan thanhToan = new ThanhToan();

        thanhToan.setIdHoaDon(hoaDon);
        thanhToan.setIdPhuongThucThanhToan(pttt);

        thanhToan.setSoTien(hoaDon.getTongThanhToan());

        thanhToan.setTrangThai("cho_thanh_toan");

        thanhToan.setNgayThanhToan(Instant.now());

        thanhToanRepository.save(thanhToan);

        return new PaymentResponse(
                true,
                "Đơn hàng sẽ thanh toán khi nhận hàng",
                null
        );
    }
}
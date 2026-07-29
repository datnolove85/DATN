package com.example.backend.Request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ThanhToanHoaDonRequest {

    private Integer idHoaDon;
    private Integer idVoucher;

    // 1. Dùng cho thanh toán cũ (Online hoặc 1 hình thức đơn lẻ)
    private Integer idPhuongThucThanhToan;

    // 2. Dùng cho thanh toán kết hợp mới (Tiền mặt + Chuyển khoản tại quầy)
    private List<ChiTietThanhTienDto> danhSachThanhToan;

    @Data
    public static class ChiTietThanhTienDto {
        private Integer idPhuongThucThanhToan;
        private BigDecimal soTien;
        private String maGiaoDich;
    }
}
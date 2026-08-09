package com.example.backend.Request;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ThanhToanHoaDonRequest {

    private Integer idHoaDon;
    private Integer idVoucher;

    // 🌟 Bổ sung trường này để nhận ID voucher cá nhân từ phía Frontend gửi lên
    private Integer idVoucherKhachHang;

    // 1. Dùng cho thanh toán cũ (Online hoặc 1 hình thức đơn lẻ)
    private Integer idPhuongThucThanhToan;

    // 2. Dùng cho thanh toán kết hợp mới (Tiền mặt + Chuyển khoản tại quầy)
    private List<ChiTietThanhTienDto> danhSachThanhToan;

    private BigDecimal tienKhachDua;
    private BigDecimal tienThoi;

    @Data
    public static class ChiTietThanhTienDto {
        private Integer idPhuongThucThanhToan;
        private BigDecimal soTien;
        private String maGiaoDich;
    }
}
package com.example.backend.Response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class HoaDonResponse {
    private Integer id;
    private Integer idKhachHang; // Hoặc trả về một Object KhachHangResponse nếu cần
    private String maHoaDon;
    private BigDecimal tongTienHang;
    private BigDecimal tongGiamGia;
    private BigDecimal phiVanChuyen;
    private BigDecimal tongThanhToan;
    private String tenNguoiNhan;
    private String soDienThoaiNguoiNhan;
    private String diaChiGiaoHang;
    private String loaiHoaDon;
    private String trangThai;
    private String ghiChu;
    private Instant ngayTao;
    private Instant ngayCapNhat;
}

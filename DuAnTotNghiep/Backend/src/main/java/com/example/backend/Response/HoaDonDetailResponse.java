package com.example.backend.Response;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonDetailResponse {

    private Integer id;
    private String maHoaDon;

    // Khách hàng
    private Integer idKhachHang;
    private String tenKhachHang;
    private String soDienThoaiKhachHang;

    // Giao hàng
    private String tenNguoiNhan;
    private String soDienThoaiNguoiNhan;
    private String diaChiGiaoHang;

    // Hóa đơn
    private String loaiHoaDon;
    private String trangThai;
    private String ghiChu;

    // Thanh toán
    private String phuongThucThanhToan;

    // Tiền
    private BigDecimal tongTienHang;
    private BigDecimal tongGiamGia;
    private BigDecimal phiVanChuyen;
    private BigDecimal tongThanhToan;

    private Instant ngayTao;
    private Instant ngayCapNhat;

    // Danh sách sản phẩm
    private List<HoaDonChiTietResponse> sanPhams;
}
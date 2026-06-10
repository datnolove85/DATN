package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonResponse {

    private Integer id;
    private Integer idKhachHang;
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
    private String phuongThucThanhToan;
}
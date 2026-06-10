package com.example.backend.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoaDonRequest {

    private Integer idKhachHang;

    private String maHoaDon;

    private BigDecimal tongTienHang;

    private BigDecimal tongGiamGia;

    private BigDecimal phiVanChuyen;

    private BigDecimal tongThanhToan;

    private String tenNguoiNhan;

    private String soDienThoaiNguoiNhan;

    private String diaChiGiaoHang;

    private String loaiHoaDon; // online / tai_quay

    private String trangThai; // cho_xac_nhan / da_xac_nhan ...

    private String ghiChu;
}
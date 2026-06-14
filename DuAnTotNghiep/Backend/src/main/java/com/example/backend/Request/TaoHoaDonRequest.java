package com.example.backend.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TaoHoaDonRequest {

    private Integer idHoaDon;
    private Integer idKhachHang;
    private String loaiHoaDon;      // tai_quay
    private Integer phuongThucTt;    // tien_mat

    private BigDecimal tongTienHang;
    private BigDecimal tongGiamGia;
    private BigDecimal tongThanhToan;

    private List<ChiTietMua> chiTietMua;
}
package com.example.backend.Response;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DonHangInfoDTO {

    private Integer id;

    private String maHoaDon;

    private String loaiHoaDon;

    private String trangThai;

    private String trangThaiHienThi;

    private String trangThaiThanhToan;

    private String trangThaiThanhToanHienThi;

    private BigDecimal tongTienHang;

    private BigDecimal tongGiamGia;

    private BigDecimal phiVanChuyen;

    private BigDecimal tongThanhToan;

    private String ghiChu;

    private LocalDateTime ngayTao;

    private LocalDateTime ngayCapNhat;

}
package com.example.backend.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DonHangChiTietDTO {

    private Integer idHoaDonChiTiet;

    private Integer idSanPhamChiTiet;

    private String maSanPham;

    private String tenSanPham;

    private String mauSac;

    private String kichThuoc;

    private String anh;

    private Integer soLuong;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

}
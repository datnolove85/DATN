package com.example.backend.Response;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietResponse {

    private Integer id;

    private Integer idSanPhamChiTiet;

    private String maSanPhamChiTiet;

    private String tenSanPhamChiTiet;

    private String tenMauSac;

    private String tenKichThuoc;

    private String tenThuongHieu;

    private Integer soLuong;

    private BigDecimal donGia;

    private BigDecimal thanhTien;
}
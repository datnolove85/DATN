package com.example.backend.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SanPhamGiamGiaResponse {

    private Integer id;

    private Integer idDotGiamGia;

    private Integer idSanPham;

    private Integer idSanPhamChiTiet;

    private String maSanPham;

    private String tenSanPham;

    private String maSPCT;

    private BigDecimal giaGoc;

    private BigDecimal giaSauGiam;

    private BigDecimal phanTramGiam;

    private Integer soLuongTon;

    private String anh;

    private String mauSac;
    private String thuongHieu;

    private String chatLieu;
    private String kichThuoc;

}
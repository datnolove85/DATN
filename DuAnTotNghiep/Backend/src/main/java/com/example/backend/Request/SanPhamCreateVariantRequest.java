package com.example.backend.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SanPhamCreateVariantRequest {

    private Integer idSanPham;

    private Integer idMauSac;
    private Integer idKichThuoc;

    private String maSanPhamChiTiet;
    private String tenSanPhamChiTiet;

    private BigDecimal giaNhap;
    private BigDecimal giaBan;

    private Integer soLuongTon;

}
package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamChiTietResponse {

    private Integer id;

    private String maSanPhamChiTiet;

    private String tenSanPham;

    private String tenMauSac;

    private String tenKichThuoc;

    private BigDecimal giaBan;

    private Integer soLuongTon;

    private List<String> images;
}
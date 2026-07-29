package com.example.backend.Request;

import lombok.Data;

@Data
public class ThemSanPhamRequest {

    private Integer idHoaDon;

    private Integer idSanPhamChiTiet;

    private Integer soLuong;
}
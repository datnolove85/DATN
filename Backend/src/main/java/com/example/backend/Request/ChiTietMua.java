package com.example.backend.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChiTietMua {
    private Integer idSanPhamChiTiet;
    private Integer soLuong;
    private BigDecimal donGia;
}
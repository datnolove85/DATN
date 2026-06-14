package com.example.backend.Response;

import com.example.backend.Entity.SanPhamChiTiet;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GioHangResponse {

    private Integer id; // id hoa_don_chi_tiet

    private SanPhamChiTiet sanPhamChiTiet;

    private String tenSanPham;

    private BigDecimal giaBan;

    private Integer soLuong;

    private BigDecimal thanhTien;
}
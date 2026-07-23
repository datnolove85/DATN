package com.example.backend.Response.thongke;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private BigDecimal tongDoanhThu;

    private BigDecimal doanhThuTienMat;

    private BigDecimal doanhThuChuyenKhoan;

    private BigDecimal loiNhuanGop;

    private Long tongDonHang;

    private Long tongKhachHang;

    private Long tongSanPham;
}
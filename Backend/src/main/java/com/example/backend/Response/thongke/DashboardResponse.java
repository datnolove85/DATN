package com.example.backend.Response.thongke;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    // ================== DOANH THU ==================
    private BigDecimal tongDoanhThu;
    private BigDecimal doanhThuTienMat;
    private BigDecimal doanhThuChuyenKhoan;
    private BigDecimal doanhThuKyTruoc;
    private BigDecimal tangTruongDoanhThu;

    // ================== LỢI NHUẬN ==================
    private BigDecimal loiNhuanGop;
    private BigDecimal giaVon;
    private BigDecimal bienLoiNhuan;
    private BigDecimal loiNhuanKyTruoc;
    private BigDecimal tangTruongLoiNhuan;

    // ================== ĐƠN HÀNG ==================
    private Long tongDonHang;
    private Long donHangOnline;
    private Long donHangTaiQuay;

    // ================== KHÁCH HÀNG ==================
    private Long tongKhachHang;
    private Long khachThanhVien;
    private Long khachLe;

    // ================== SẢN PHẨM ==================
    private Long tongSanPham;          // Tổng số lượng sản phẩm bán
    private Long skuDaBan;             // Số mẫu sản phẩm khác nhau đã bán
    private BigDecimal trungBinhSpMoiDon;
}
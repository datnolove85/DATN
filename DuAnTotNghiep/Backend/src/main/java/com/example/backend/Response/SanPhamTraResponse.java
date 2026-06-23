package com.example.backend.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SanPhamTraResponse {

    // HDCT
    private Integer hdctId;

    // SPCT
    private Integer spctId;

    // Thông tin sản phẩm
    private String tenSanPham;
    private String thuongHieu;
    private String mauSac;
    private String kichThuoc;
    private String chatLieu;

    // Giá lúc mua
    private BigDecimal donGia;

    // Số lượng
    private Integer soLuongMua;
    private Integer daTra;
    private Integer conLai;

    // Thành tiền của dòng hóa đơn
    private BigDecimal thanhTien;

    private Integer soLuongTra;
}
package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GioHangOnlineResponse {

    private Integer id;
    private Integer idSanPhamChiTiet;
    private String tenSanPham;
    private String maSanPhamChiTiet;
    private String thuongHieu;
    private String mauSac;
    private String kichCo;
    private String anh;

    private BigDecimal giaBan;        // Giá gốc
    private BigDecimal giaSauGiam;    // Giá sau khi trừ giảm giá
    private Integer phanTramGiam;     // % Giảm giá của đợt giảm giá (VD: 20 -> 20%)
    private Boolean dangGiamGia;      // true/false

    private Integer soLuong;
    private Integer soLuongTon;
    private Integer soLuongKhaDung;
    private BigDecimal thanhTien;
}
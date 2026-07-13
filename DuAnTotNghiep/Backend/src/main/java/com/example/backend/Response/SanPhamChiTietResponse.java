package com.example.backend.Response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietResponse {

    private Integer id;

    private Integer idSanPham;

    private String tenSanPham;

    private String tenDanhMuc;

    private String tenThuongHieu;

    private String tenChatLieu;

    private Integer idMauSac;
    private String tenMauSac;

    private Integer idKichThuoc;
    private String tenKichThuoc;

    private String maSanPhamChiTiet;

    private String tenSanPhamChiTiet;

    private BigDecimal giaNhap;

    private BigDecimal giaBan;

    private Integer soLuongTon;

    private Boolean trangThai;

    private List<String> images;

    private BigDecimal giaSauGiam;

    private Integer phanTramGiam;

    private Boolean dangGiamGia;
}
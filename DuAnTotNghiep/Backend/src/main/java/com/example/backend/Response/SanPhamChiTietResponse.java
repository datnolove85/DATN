package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data

public class SanPhamChiTietResponse {

    private Integer id;
    private String maSanPhamChiTiet;
    private String tenSanPham;

    private Integer idDanhMuc;
    private String tenDanhMuc;

    private String tenMauSac;
    private String tenKichThuoc;

    private String tenChatLieu;     // 👈 thêm
    private String tenThuongHieu;   // 👈 thêm

    private BigDecimal giaBan;
    private Integer soLuongTon;

    private List<String> images;

    private List<VariantResponse> variants;

    public SanPhamChiTietResponse(
            Integer id,
            String maSanPhamChiTiet,
            String tenSanPham,
            Integer idDanhMuc,
            String tenDanhMuc,
            String tenMauSac,
            String tenKichThuoc,
            String tenChatLieu,
            String tenThuongHieu,
            BigDecimal giaBan,
            Integer soLuongTon,
            List<String> images,
            List<VariantResponse> variants
    ) {
        this.id = id;
        this.maSanPhamChiTiet = maSanPhamChiTiet;
        this.tenSanPham = tenSanPham;
        this.idDanhMuc = idDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
        this.tenMauSac = tenMauSac;
        this.tenKichThuoc = tenKichThuoc;
        this.tenChatLieu = tenChatLieu;
        this.tenThuongHieu = tenThuongHieu;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
        this.images = images;
        this.variants = variants;
    }
}
package com.example.backend.Response.thongke;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TopProductStatistic {

    private Integer productId;
    private String maSanPham;
    private String thuongHieu;
    private String chatLieu;
    private String tenSanPham;
    private Long soLuongBan;
    private BigDecimal doanhThu;
    private BigDecimal giaVon; // Chuyển lại về BigDecimal cho chuẩn kiểu tiền tệ

    public TopProductStatistic(
            Integer productId,
            String maSanPham,
            String thuongHieu,
            String chatLieu,
            String tenSanPham,
            Long soLuongBan,
            BigDecimal doanhThu,
            BigDecimal giaVon) {

        this.productId = productId;
        this.maSanPham = maSanPham;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.tenSanPham = tenSanPham;
        this.soLuongBan = soLuongBan;
        this.doanhThu = doanhThu;
        this.giaVon = giaVon;
    }
}
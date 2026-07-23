package com.example.backend.Response.thongke;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopProductStatistic {

    private Integer productId;

    private String maSanPham;

    private String thuongHieu;

    private String chatLieu;

    private String tenSanPham;

    private Long soLuongBan;

    private BigDecimal doanhThu;

}
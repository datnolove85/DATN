package com.example.backend.Response.thongke;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TopProductResponse {

    private Integer productId;

    private String maSanPham;

    private String tenSanPham;

    private String thuongHieu;

    private String chatLieu;

    private String anh;

    private BigDecimal giaBan;

    private Long soBienThe;

    private Long tongTonKho;

    private Long soLuongBan;

    private BigDecimal doanhThu;

    private BigDecimal loiNhuan;

}
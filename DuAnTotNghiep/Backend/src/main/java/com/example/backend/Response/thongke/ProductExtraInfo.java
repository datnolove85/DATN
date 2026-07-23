package com.example.backend.Response.thongke;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductExtraInfo {

    private Integer productId;

    private BigDecimal giaBan;

    private BigDecimal giaNhap;

    private Long soBienThe;

    private Long tongTonKho;

}
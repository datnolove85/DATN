package com.example.backend.Response;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonVoucherResponse {

    private Integer id;
    private String tenVoucher;
    private String maCode;
    private String loaiGiamGia;
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriGiamToiDa;
    private BigDecimal dieuKienToiThieu;
    private BigDecimal soTienGiam;

    // Phân biệt nguồn voucher
    private String loaiVoucher; // HE_THONG hoặc MINIGAME
}
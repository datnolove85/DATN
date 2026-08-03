package com.example.backend.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoucherKhachHangResponse {
    private Integer id; // id kho voucher
    private Integer idVoucherKhachHang;

    private String tenVoucher;
    private String maCode;
    private String loaiGiamGia;

    private BigDecimal giaTriGiam;
    private BigDecimal giaTriGiamToiDa;
    private BigDecimal dieuKienToiThieu;

    private Integer soXuDoi;
    private Integer soLuongConLai;

    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayHetHan;

    private String moTa;
    private Boolean trangThai;
}

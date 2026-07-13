package com.example.backend.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherRequest {
    private String tenVoucher;
    private String loaiGiamGia;
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriDonHangToiThieu;
    private BigDecimal giaTriGiamToiDa;
    private Integer soLuong;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String moTa;
    private Integer trangThai;
}
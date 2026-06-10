package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoucherResponse {
    private Integer id;
    private String maVoucher;
    private String tenVoucher;
    private String loaiGiamGia;
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriDonHangToiThieu;
    private BigDecimal giaTriGiamToiDa;
    private Integer soLuong;
    private Integer soLuongDaDung;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private String moTa;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private Integer trangThai;
}
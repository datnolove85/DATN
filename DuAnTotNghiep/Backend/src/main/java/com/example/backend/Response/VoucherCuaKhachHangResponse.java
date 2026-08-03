package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor

public class VoucherCuaKhachHangResponse {

    // Thông tin voucher của khách
    private Integer idVoucherKhachHang;
    private String trangThai;
    private LocalDateTime ngayDoi;

    // Thông tin kho voucher
    private Integer idKhoVoucher;
    private String tenVoucher;
    private String maCode;

    private String loaiGiamGia;
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriGiamToiDa;
    private BigDecimal dieuKienToiThieu;

    private Integer soXuDoi;

    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayHetHan;

    private String moTa;

    // Hiển thị đẹp ngoài FE
    private String hienThiGiaTriGiam;
    private Boolean sapHetHan;
    private Long soNgayConLai;



    // Getter Setter
}
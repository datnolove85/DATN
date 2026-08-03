package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "hoa_don_voucher")
public class HoaDonVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_hoa_don", nullable = false)
    private HoaDon idHoaDon;

    // Voucher hệ thống
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voucher")
    private Voucher idVoucher;

    // Kho voucher (voucher đổi xu)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kho_voucher")
    private KhoVoucher idKhoVoucher;

    // Voucher cụ thể của khách
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voucher_khach_hang")
    private VoucherCuaKhachHang voucherCuaKhachHang;

    @NotNull
    @Column(name = "so_tien_giam", nullable = false, precision = 18, scale = 2)
    private BigDecimal soTienGiam;

    // Đã chính thức sử dụng voucher hay chưa
    @Column(name = "da_consume", nullable = false)
    private Boolean daConsume = false;
}
package com.example.backend;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "ma_voucher", nullable = false, length = 30)
    private String maVoucher;

    @Size(max = 200)
    @NotNull
    @Nationalized
    @Column(name = "ten_voucher", nullable = false, length = 200)
    private String tenVoucher;

    @Size(max = 20)
    @NotNull
    @Column(name = "loai_giam_gia", nullable = false, length = 20)
    private String loaiGiamGia;

    @NotNull
    @Column(name = "gia_tri_giam", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTriGiam;

    @ColumnDefault("0")
    @Column(name = "gia_tri_don_hang_toi_thieu", precision = 18, scale = 2)
    private BigDecimal giaTriDonHangToiThieu;

    @Column(name = "gia_tri_giam_toi_da", precision = 18, scale = 2)
    private BigDecimal giaTriGiamToiDa;

    @ColumnDefault("0")
    @Column(name = "so_luong")
    private Integer soLuong;

    @ColumnDefault("0")
    @Column(name = "so_luong_da_dung")
    private Integer soLuongDaDung;

    @Column(name = "ngay_bat_dau")
    private Instant ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private Instant ngayKetThuc;

    @Nationalized
    @Lob
    @Column(name = "mo_ta")
    private String moTa;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

}
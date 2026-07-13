package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dot_giam_gia")
public class DotGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "ma_dot_giam_gia")
    private String maDotGiamGia;


    @Column(name = "ten_dot_giam_gia")
    private String tenDotGiamGia;


    @Column(name = "loai_giam_gia")
    private String loaiGiamGia;


    @Column(name = "gia_tri_giam")
    private BigDecimal giaTriGiam;


    @Column(name = "gia_tri_giam_toi_da")
    private BigDecimal giaTriGiamToiDa;


    @Column(name = "ngay_bat_dau")
    private Instant ngayBatDau;


    @Column(name = "ngay_ket_thuc")
    private Instant ngayKetThuc;


    @Column(name = "mo_ta")
    private String moTa;


    @Column(name = "ngay_tao")
    private Instant ngayTao;


    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;


    @Column(name = "trang_thai")
    private String trangThai;


    @OneToMany(
            mappedBy = "dotGiamGia",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SanPhamGiamGia> sanPhamGiamGias;

}
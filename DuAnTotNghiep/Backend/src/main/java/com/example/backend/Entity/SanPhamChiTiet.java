package com.example.backend.Entity;

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
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "ma_san_pham_chi_tiet", nullable = false, length = 30)
    private String maSanPhamChiTiet;

    @Size(max = 200)
    @Nationalized
    @Column(name = "ten_san_pham_chi_tiet", length = 200)
    private String tenSanPhamChiTiet;

    @NotNull
    @Column(name = "gia_nhap", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaNhap;

    @NotNull
    @Column(name = "gia_goc", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaGoc;

    @NotNull
    @Column(name = "gia_ban", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaBan;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "so_luong_ton", nullable = false)
    private Integer soLuongTon;

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
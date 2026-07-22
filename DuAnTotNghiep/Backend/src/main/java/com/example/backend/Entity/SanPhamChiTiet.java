package com.example.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_san_pham", nullable = false)
    private SanPham idSanPham;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_mau_sac", nullable = false)
    private MauSac idMauSac;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_kich_thuoc", nullable = false)
    private KichThuoc idKichThuoc;

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

    @OneToMany(mappedBy = "sanPhamChiTiet")
    @JsonIgnore
    private List<SanPhamGiamGia> sanPhamGiamGias;
}
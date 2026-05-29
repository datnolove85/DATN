package com.example.backend;

import com.example.backend.Entity.KhachHang;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "dia_chi_giao_hang")
public class DiaChiGiaoHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang idKhachHang;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "ten_nguoi_nhan", nullable = false, length = 100)
    private String tenNguoiNhan;

    @Size(max = 15)
    @NotNull
    @Column(name = "so_dien_thoai", nullable = false, length = 15)
    private String soDienThoai;

    @Size(max = 100)
    @Nationalized
    @Column(name = "tinh_thanh", length = 100)
    private String tinhThanh;

    @Size(max = 100)
    @Nationalized
    @Column(name = "quan_huyen", length = 100)
    private String quanHuyen;

    @Size(max = 100)
    @Nationalized
    @Column(name = "phuong_xa", length = 100)
    private String phuongXa;

    @Size(max = 500)
    @NotNull
    @Nationalized
    @Column(name = "dia_chi_cu_the", nullable = false, length = 500)
    private String diaChiCuThe;

    @ColumnDefault("0")
    @Column(name = "mac_dinh")
    private Boolean macDinh;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

}
package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan idTaiKhoan;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ma_khach_hang", length = 50)
    private String maKhachHang;

    @Size(max = 150)
    @NotNull
    @Column(name = "ho_ten", nullable = false, length = 150)
    private String hoTen;

    @Size(max = 20)
    @NotNull
    @Column(name = "so_dien_thoai", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Size(max = 50)
    @Column(name = "hang_thanh_vien", length = 50)
    private String hangThanhVien;

    @Size(max = 255)
    @Column(name = "dia_chi")
    private String diaChi;

    @ColumnDefault("0")
    @Column(name = "so_lan_mua")
    private Integer soLanMua;

    @Column(name = "ngay_mua_cuoi")
    private Instant ngayMuaCuoi;

    @Size(max = 255)
    @Column(name = "anh")
    private String anh;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    // === BỔ SUNG: Ví Xu & Chuỗi điểm danh ===
    @ColumnDefault("0")
    @Column(name = "so_du_xu")
    private Integer soDuXu = 0;

    @ColumnDefault("0")
    @Column(name = "chuoi_diem_danh")
    private Integer chuoiDiemDanh = 0;

    @Column(name = "ngay_diem_danh_gan_nhat")
    private LocalDate ngayDiemDanhGanNhat;
    // =====================================

    // === BỔ SUNG MỚI: Quản lý số lượt lật thẻ trong ngày ===

    @Column(name = "so_luot_lat_the")
    private Integer soLuotLatThe;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

    @Size(max = 100)
    @Column(name = "nguoi_tao", length = 100)
    private String nguoiTao;

    @Size(max = 100)
    @Column(name = "nguoi_cap_nhat", length = 100)
    private String nguoiCapNhat;

    @ColumnDefault("0")
    @Column(name = "trang_thai_xoa")
    private Boolean trangThai;
}
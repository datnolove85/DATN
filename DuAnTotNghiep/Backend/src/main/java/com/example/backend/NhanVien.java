package com.example.backend;

import com.example.backend.Entity.TaiKhoan;
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
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_tai_khoan", nullable = false)
    private TaiKhoan idTaiKhoan;

    @Size(max = 20)
    @NotNull
    @Column(name = "ma_nhan_vien", nullable = false, length = 20)
    private String maNhanVien;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Size(max = 255)
    @Nationalized
    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "gioi_tinh")
    private Boolean gioiTinh;

    @Size(max = 20)
    @Column(name = "can_cuoc_cong_dan", length = 20)
    private String canCuocCongDan;

    @Size(max = 255)
    @Nationalized
    @Column(name = "anh")
    private String anh;

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
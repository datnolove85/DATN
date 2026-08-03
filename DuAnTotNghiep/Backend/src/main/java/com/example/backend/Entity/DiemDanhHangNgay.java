package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "diem_danh_hang_ngay")
public class DiemDanhHangNgay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang idKhachHang;

    @NotNull
    @Column(name = "ngay_diem_danh", nullable = false)
    private LocalDate ngayDiemDanh;

    @NotNull
    @Column(name = "so_xu_nhan", nullable = false)
    private Integer soXuNhan;

    @NotNull
    @Column(name = "so_ngay_lien_tiep", nullable = false)
    private Integer soNgayLienTiep;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao = LocalDateTime.now();
}
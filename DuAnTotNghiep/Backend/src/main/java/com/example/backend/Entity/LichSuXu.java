package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "lich_su_xu")
public class LichSuXu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang idKhachHang;

    @NotNull
    @Column(name = "so_xu_thay_doi", nullable = false)
    private Integer soXuThayDoi;

    @NotNull
    @Column(name = "so_du_truoc", nullable = false)
    private Integer soDuTruoc;

    @NotNull
    @Column(name = "so_du_sau", nullable = false)
    private Integer soDuSau;

    @Size(max = 50)
    @NotNull
    @Column(name = "loai_giao_dich", nullable = false, length = 50)
    private String loaiGiaoDich;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao = LocalDateTime.now();
}
package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "lich_su_minigame")
public class LichSuMinigame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang idKhachHang;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_phan_thuong", nullable = false)
    private PhanThuongMinigame idPhanThuong;

    @Size(max = 30)
    @NotNull
    @Column(name = "loai_game", nullable = false, length = 30)
    private String loaiGame;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Size(max = 30)
    @ColumnDefault("'da_nhan'")
    @Column(name = "trang_thai_nhan", length = 30)
    private String trangThaiNhan = "da_nhan";
}
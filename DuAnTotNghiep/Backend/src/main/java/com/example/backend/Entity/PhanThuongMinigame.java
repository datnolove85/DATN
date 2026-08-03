package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "phan_thuong_minigame")
public class PhanThuongMinigame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Nationalized
    @Column(name = "ten_phan_thuong", nullable = false, length = 150)
    private String tenPhanThuong;

    @Size(max = 30)
    @NotNull
    @Column(name = "loai_game", nullable = false, length = 30)
    private String loaiGame; // 'vong_quay' hoặc 'lat_the'

    @Size(max = 30)
    @NotNull
    @Column(name = "loai_phan_thuong", nullable = false, length = 30)
    private String loaiPhanThuong; // 'xu', 'voucher', 'khong_trung', 'vat_pham'

    @ColumnDefault("0")
    @Column(name = "gia_tri_xu")
    private Integer giaTriXu = 0;

    @Column(name = "id_voucher")
    private Integer idVoucher;

    @NotNull
    @Column(name = "ty_le_trung", nullable = false, precision = 5, scale = 2)
    private BigDecimal tyLeTrung;

    @ColumnDefault("-1")
    @Column(name = "so_luong_gioi_han")
    private Integer soLuongGioiHan = -1;

    @ColumnDefault("0")
    @Column(name = "so_luong_da_trung")
    private Integer soLuongDaTrung = 0;

    @Size(max = 255)
    @Column(name = "anh")
    private String anh;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai = true;
}
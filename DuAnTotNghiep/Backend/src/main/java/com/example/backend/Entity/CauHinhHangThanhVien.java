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
@Table(name = "cau_hinh_hang_thanh_vien")
public class CauHinhHangThanhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "ten_hang", nullable = false, length = 50)
    private String tenHang;

    @ColumnDefault("0")
    @Column(name = "nguong_chi_tieu_toi_thieu", precision = 18, scale = 2)
    private BigDecimal nguongChiTieuToiThieu = BigDecimal.ZERO;

    @ColumnDefault("1.0")
    @Column(name = "ty_le_tich_xu")
    private Double tyLeTichXu = 1.0;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai = true;
}
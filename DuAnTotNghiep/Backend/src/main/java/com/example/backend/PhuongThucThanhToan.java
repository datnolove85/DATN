package com.example.backend;

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
@Table(name = "phuong_thuc_thanh_toan")
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "ma_phuong_thuc", nullable = false, length = 20)
    private String maPhuongThuc;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "ten_phuong_thuc", nullable = false, length = 100)
    private String tenPhuongThuc;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

}
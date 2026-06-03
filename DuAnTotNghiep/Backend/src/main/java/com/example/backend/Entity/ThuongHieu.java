package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "thuong_hieu")
public class ThuongHieu extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "ma_thuong_hieu", nullable = false, length = 20)
    private String maThuongHieu;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "ten_thuong_hieu", nullable = false, length = 100)
    private String tenThuongHieu;

}
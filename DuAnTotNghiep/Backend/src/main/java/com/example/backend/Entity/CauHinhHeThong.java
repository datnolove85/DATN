package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cau_hinh_he_thong")
public class CauHinhHeThong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 50)
    @Column(name = "ma_cau_hinh", length = 50, unique = true)
    private String maCauHinh;

    @Column(name = "gia_tri_so", precision = 18, scale = 2)
    private BigDecimal giaTriSo;

    @Size(max = 255)
    @Nationalized
    @Column(name = "mo_ta")
    private String moTa;
}
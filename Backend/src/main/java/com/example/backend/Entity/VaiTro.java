package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "vai_tro")
@Getter
@Setter
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_vai_tro", nullable = false)
    private String maVaiTro;

    @Column(name = "ten_vai_tro", nullable = false)
    private String tenVaiTro;

    @Column(name = "trang_thai")
    private Integer trangThai;
}
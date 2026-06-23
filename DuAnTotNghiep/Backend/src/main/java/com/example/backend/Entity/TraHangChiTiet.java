package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Table(name = "tra_hang_chi_tiet")
@Getter
@Setter
public class TraHangChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tra_hang_id", nullable = false)
    private TraHang traHang;

    @ManyToOne
    @JoinColumn(name = "hoa_don_chi_tiet_id", nullable = false)
    private HoaDonChiTiet hoaDonChiTiet;

    @Column(name = "so_luong_tra", nullable = false)
    private Integer soLuongTra;

    @Column(name = "don_gia_tra", nullable = false)
    private BigDecimal donGiaTra;

    @Column(name = "thanh_tien", nullable = false)
    private BigDecimal thanhTien;
}
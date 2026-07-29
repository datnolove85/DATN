package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tra_hang")
@Getter
@Setter
public class TraHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_tra_hang", nullable = false)
    private String maTraHang;

    @ManyToOne
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private HoaDon hoaDon;

    @Column(name = "ngay_tra", nullable = false)
    private LocalDateTime ngayTra;

    @Column(name = "tong_tien_hoan", nullable = false)
    private BigDecimal tongTienHoan = BigDecimal.ZERO;

    @Column(name = "ly_do")
    private String lyDo;

    @Column(name = "trang_thai", nullable = false)
    private String trangThai;

    // ⭐ QUAN TRỌNG: CÁI NÀY LÀ KEY
    @OneToMany(mappedBy = "traHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TraHangChiTiet> chiTiets = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (maTraHang == null) {
            maTraHang = "TH" + System.currentTimeMillis();
        }
        if (ngayTra == null) {
            ngayTra = LocalDateTime.now();
        }
        if (tongTienHoan == null) {
            tongTienHoan = BigDecimal.ZERO;
        }
        if (trangThai == null) {
            trangThai = "DA_TRA";
        }
    }
}
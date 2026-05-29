package com.example.backend.Entity;

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
@Table(name = "hinh_anh")
public class HinhAnh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_san_pham_chi_tiet", nullable = false)
    private com.example.backend.Entity.SanPhamChiTiet idSanPhamChiTiet;

    @Size(max = 100)
    @Nationalized
    @Column(name = "ten_anh", length = 100)
    private String tenAnh;

    @Size(max = 500)
    @NotNull
    @Nationalized
    @Column(name = "link", nullable = false, length = 500)
    private String link;

    @Size(max = 20)
    @Column(name = "dinh_dang", length = 20)
    private String dinhDang;

    @ColumnDefault("0")
    @Column(name = "la_anh_chinh")
    private Boolean laAnhChinh;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

}
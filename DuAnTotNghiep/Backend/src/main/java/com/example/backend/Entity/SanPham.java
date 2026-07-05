package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_danh_muc", nullable = false)
    private DanhMuc idDanhMuc;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_thuong_hieu", nullable = false)
    private ThuongHieu idThuongHieu;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_chat_lieu", nullable = false)
    private ChatLieu idChatLieu;

    @Size(max = 20)
    @Column(name = "ma_san_pham",length = 20)
    private String maSanPham;

    @Size(max = 200)
    @NotNull
    @Nationalized
    @Column(name = "ten_san_pham", nullable = false, length = 200)
    private String tenSanPham;

    @Nationalized
    @Lob
    @Column(name = "mo_ta")
    private String moTa;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Date ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_cap_nhat")
    private Date ngayCapNhat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien_tao")
    private NhanVien idNhanVienTao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien_cap_nhat")
    private NhanVien idNhanVienCapNhat;

    @ColumnDefault("1")
    @Column(name = "trang_thai")
    private Boolean trangThai;

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        ngayTao = now;
        ngayCapNhat = now;

        if (trangThai == null) {
            trangThai = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        ngayCapNhat = new Date();
    }

}
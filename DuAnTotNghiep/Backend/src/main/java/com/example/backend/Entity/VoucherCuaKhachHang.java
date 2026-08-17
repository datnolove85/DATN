
        package com.example.backend.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher_cua_khach_hang")
public class VoucherCuaKhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_khach_hang", nullable = false)
    private Integer idKhachHang;

    @Column(name = "id_voucher_minigame", nullable = false)
    private Integer idVoucherMinigame;

    @Column(name = "trang_thai")
    private String trangThai;

    @Column(name = "ngay_doi")
    private LocalDateTime ngayDoi;

    public VoucherCuaKhachHang() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(Integer idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public Integer getIdVoucherMinigame() {
        return idVoucherMinigame;
    }

    public void setIdVoucherMinigame(Integer idVoucherMinigame) {
        this.idVoucherMinigame = idVoucherMinigame;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayDoi() {
        return ngayDoi;
    }

    public void setNgayDoi(LocalDateTime ngayDoi) {
        this.ngayDoi = ngayDoi;
    }
}


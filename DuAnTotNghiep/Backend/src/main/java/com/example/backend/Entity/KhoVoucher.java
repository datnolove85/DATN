package com.example.backend.Entity; // Thay đổi package cho phù hợp với dự án của bạn

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "kho_voucher")
public class KhoVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Nationalized
    @Column(name = "ten_voucher", nullable = false)
    private String tenVoucher;

    @Column(name = "ma_code", unique = true, nullable = false)
    private String maCode;

    @Column(name = "loai_giam_gia", nullable = false, length = 20)
    private String loaiGiamGia; // 'tien_mat' hoặc 'phan_tram'

    @Column(name = "gia_tri_giam", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTriGiam;

    @Column(name = "gia_tri_giam_toi_da", precision = 18, scale = 2)
    private BigDecimal giaTriGiamToiDa; // Dùng khi giảm theo phần trăm

    @Column(name = "dieu_kien_toi_thieu", precision = 18, scale = 2)
    private BigDecimal dieuKienToiThieu;

    @Column(name = "so_xu_doi", nullable = false)
    private Integer soXuDoi;

    @Column(name = "so_luong_con_lai", nullable = false)
    private Integer soLuongConLai;

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_het_han", nullable = false)
    private LocalDateTime ngayHetHan;

    @Nationalized
    @Lob
    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    // Constructors
    public KhoVoucher() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTenVoucher() { return tenVoucher; }
    public void setTenVoucher(String tenVoucher) { this.tenVoucher = tenVoucher; }

    public String getMaCode() { return maCode; }
    public void setMaCode(String maCode) { this.maCode = maCode; }

    public String getLoaiGiamGia() { return loaiGiamGia; }
    public void setLoaiGiamGia(String loaiGiamGia) { this.loaiGiamGia = loaiGiamGia; }

    public BigDecimal getGiaTriGiam() { return giaTriGiam; }
    public void setGiaTriGiam(BigDecimal giaTriGiam) { this.giaTriGiam = giaTriGiam; }

    public BigDecimal getGiaTriGiamToiDa() { return giaTriGiamToiDa; }
    public void setGiaTriGiamToiDa(BigDecimal giaTriGiamToiDa) { this.giaTriGiamToiDa = giaTriGiamToiDa; }

    public BigDecimal getDieuKienToiThieu() { return dieuKienToiThieu; }
    public void setDieuKienToiThieu(BigDecimal dieuKienToiThieu) { this.dieuKienToiThieu = dieuKienToiThieu; }

    public Integer getSoXuDoi() { return soXuDoi; }
    public void setSoXuDoi(Integer soXuDoi) { this.soXuDoi = soXuDoi; }

    public Integer getSoLuongConLai() { return soLuongConLai; }
    public void setSoLuongConLai(Integer soLuongConLai) { this.soLuongConLai = soLuongConLai; }

    public LocalDateTime getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDateTime ngayBatDau) { this.ngayBatDau = ngayBatDau; }

    public LocalDateTime getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(LocalDateTime ngayHetHan) { this.ngayHetHan = ngayHetHan; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public LocalDateTime getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(LocalDateTime ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }
}
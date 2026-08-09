package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien")
    private NhanVien idNhanVien;

    @Size(max = 30)
    @NotNull
    @Column(name = "ma_hoa_don", nullable = false, length = 30)
    private String maHoaDon;

    @ColumnDefault("0")
    @Column(name = "tong_tien_hang", precision = 18, scale = 2)
    private BigDecimal tongTienHang;

    @ColumnDefault("0")
    @Column(name = "tong_giam_gia", precision = 18, scale = 2)
    private BigDecimal tongGiamGia;

    @ColumnDefault("0")
    @Column(name = "phi_van_chuyen", precision = 18, scale = 2)
    private BigDecimal phiVanChuyen;

    @ColumnDefault("0")
    @Column(name = "tong_thanh_toan", precision = 18, scale = 2)
    private BigDecimal tongThanhToan;

    // === BỔ SUNG: Dùng xu thanh toán đơn hàng ===
    @ColumnDefault("0")
    @Column(name = "so_xu_su_dung")
    private Integer soXuSuDung = 0;

    @ColumnDefault("0")
    @Column(name = "tien_giam_do_xu", precision = 18, scale = 2)
    private BigDecimal tienGiamDoXu = BigDecimal.ZERO;
    // ============================================

    // === BỔ SUNG MỚI: Tiền khách đưa & Tiền thối lại ===
    @ColumnDefault("0")
    @Column(name = "tien_khach_dua", precision = 18, scale = 2)
    private BigDecimal tienKhachDua = BigDecimal.ZERO;

    @ColumnDefault("0")
    @Column(name = "tien_thoi_lai", precision = 18, scale = 2)
    private BigDecimal tienThoiLai = BigDecimal.ZERO;
    // ===================================================

    @Size(max = 100)
    @Nationalized
    @Column(name = "ten_nguoi_nhan", length = 100)
    private String tenNguoiNhan;

    @Size(max = 15)
    @Column(name = "so_dien_thoai_nguoi_nhan", length = 15)
    private String soDienThoaiNguoiNhan;

    @Size(max = 500)
    @Nationalized
    @Column(name = "dia_chi_giao_hang", length = 500)
    private String diaChiGiaoHang;

    @Size(max = 20)
    @Column(name = "loai_hoa_don", length = 20)
    private String loaiHoaDon;

    @Size(max = 30)
    @Column(name = "trang_thai", length = 30)
    private String trangThai;

    @Nationalized
    @Lob
    @Column(name = "ghi_chu")
    private String ghiChu;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Size(max = 30)
    @ColumnDefault("'chua_thanh_toan'")
    @Column(name = "trang_thai_thanh_toan", length = 30)
    private String trangThaiThanhToan;
}
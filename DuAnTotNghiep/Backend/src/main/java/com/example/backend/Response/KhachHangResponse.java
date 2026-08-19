package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KhachHangResponse {
    private Integer id;
    private String maKhachHang;
    private String hoTen;
    private Integer idTaiKhoan;
    private String email;
    private String tenTaiKhoan;
    private String soDienThoai;
    private LocalDate ngaySinh;
    private String anh;

    // Thông tin địa chỉ chi tiết
    private String thanhPho;
    private String quan;
    private String phuong;
    private String diaChiCuThe;
    private String diaChiGop; // Địa chỉ gộp đầy đủ lưu ở bảng khách hàng

    private Boolean gioiTinh;
    private Boolean trangThai;

    // --- CÁC TRƯỜNG MỚI BỔ SUNG KHỚP VỚI DB ---
    private String hangThanhVien;
    private Integer soLanMua;
    private BigDecimal tongChiTieu; // Khớp kiểu BigDecimal với Entity
    private Instant ngayBatDauChuKy; // Khớp kiểu Instant với Entity
    private Instant ngayHetHanHang;  // Khớp kiểu Instant với Entity
    private Instant ngayMuaCuoi;     // Khớp kiểu Instant với Entity

    // Quản lý xu & điểm danh
    private Integer soDuXu;
    private Integer chuoiDiemDanh;
    private LocalDate ngayDiemDanhGanNhat;
    private Integer soLuotLatThe;

    private Instant ngayTao;         // Khớp kiểu Instant với Entity
}
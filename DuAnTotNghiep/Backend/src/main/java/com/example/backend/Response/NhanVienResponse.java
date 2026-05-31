package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NhanVienResponse {

    private Integer id;

    // Tài khoản
    private Integer idTaiKhoan;
    private String email;
    private String soDienThoai;

    // Vai trò
    private Integer idVaiTro;
    private String tenVaiTro;

    // Nhân viên
    private String maNhanVien;
    private String hoTen;

    // Địa chỉ
    private String diaChi;

    // Thông tin cá nhân
    private LocalDate ngaySinh;
    private Boolean gioiTinh;
    private String canCuocCongDan;
    private String anh;

    // Audit
    private Instant ngayTao;
    private Instant ngayCapNhat;

    // Trạng thái
    private Boolean trangThai;
}
package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

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
    private LocalDate ngaySinh; // Đảm bảo khớp kiểu dữ liệu đã sửa
    private String anh;
    private String thanhPho;
    private String quan;
    private String phuong;
    private String diaChiCuThe;
    private String diaChiGop; // Trường bạn truyền null ở Query
    private Boolean gioiTinh;
    private Boolean trangThai;
}

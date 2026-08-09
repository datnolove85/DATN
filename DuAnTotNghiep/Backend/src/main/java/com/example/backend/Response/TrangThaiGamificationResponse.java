package com.example.backend.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrangThaiGamificationResponse {
    private Integer idKhachHang;
    private Integer soDuXu;
    private Integer chuoiDiemDanh;
    private LocalDate ngayDiemDanhGanNhat;
    private boolean daDiemDanhHomNay; // true nếu hôm nay đã điểm danh rồi
    // Thêm trường này vào class TrangThaiGamificationResponse
    private Integer soLuotLatTheConLai;
    private int soLuotLatTheToiDa;
}
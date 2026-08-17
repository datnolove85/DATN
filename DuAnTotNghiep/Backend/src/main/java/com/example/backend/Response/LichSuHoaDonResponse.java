package com.example.backend.Response; // Hoặc package chứa Response của bạn

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LichSuHoaDonResponse {
    private Integer id;
    private String trangThaiCu;
    private String trangThaiMoi;
    private LocalDateTime thoiGian;
    private String tenNhanVien; // Tên nhân viên thực hiện (nếu có)
    private String ghiChu;
    private String nguonThaoTac; // 'STAFF', 'CUSTOMER', 'SYSTEM'
}
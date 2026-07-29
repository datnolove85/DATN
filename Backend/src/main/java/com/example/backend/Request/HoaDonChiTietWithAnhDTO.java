package com.example.backend.Request;

import com.example.backend.Entity.HoaDonChiTiet;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HoaDonChiTietWithAnhDTO {
    private HoaDonChiTiet hoaDonChiTiet;
    private String anh;
}

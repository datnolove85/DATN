package com.example.backend.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class HoaDonFilterRequest {

    private String keyword;
    private String trangThai;
    private String loaiHoaDon;

    private Double minPrice;
    private Double maxPrice;

    private LocalDate fromDate;
    private LocalDate toDate;

    private String trangThaiThanhToan;
}
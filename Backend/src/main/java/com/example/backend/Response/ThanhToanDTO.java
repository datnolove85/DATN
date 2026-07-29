package com.example.backend.Response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class ThanhToanDTO {

    private String phuongThucThanhToan;

    private String maGiaoDich;

    private BigDecimal soTien;

    private String trangThai;

    private LocalDateTime ngayThanhToan;

}
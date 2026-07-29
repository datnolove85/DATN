package com.example.backend.Request;

import lombok.Data;

@Data
public class ThanhToanHoaDonRequest {

    private Integer idHoaDon;

    private Integer idPhuongThucThanhToan;

    private Integer idVoucher;
}
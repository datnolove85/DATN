package com.example.backend.Response;

import lombok.Data;

import java.util.List;

@Data
public class TraHangResponse {

    private HoaDonDetailResponse hoaDon;

    private List<SanPhamTraResponse> sanPhams;

}
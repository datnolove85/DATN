package com.example.backend.Response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class DotGiamGiaResponse {

    private Integer id;
    private String maDotGiamGia;
    private String tenDotGiamGia;
    private String loaiGiamGia;
    private BigDecimal giaTriGiam;
    private BigDecimal giaTriGiamToiDa;
    private Instant ngayBatDau;
    private Instant ngayKetThuc;
    private String moTa;
    private Boolean trangThai;
}
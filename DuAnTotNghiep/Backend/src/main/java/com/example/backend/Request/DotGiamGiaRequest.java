package com.example.backend.Request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class DotGiamGiaRequest {

    @NotBlank
    private String maDotGiamGia;

    @NotBlank
    private String tenDotGiamGia;

    @NotBlank
    private String loaiGiamGia; // %, VND

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal giaTriGiam;

    private BigDecimal giaTriGiamToiDa;

    @NotNull
    private Instant ngayBatDau;

    @NotNull
    private Instant ngayKetThuc;

    private String moTa;

    private Boolean trangThai;
}
package com.example.backend.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class CreateDotGiamGiaRequest {

    @NotBlank(message = "Tên đợt giảm giá không được để trống")
    private String tenDotGiamGia;

    @NotBlank(message = "Loại giảm giá không được để trống")
    private String loaiGiamGia;

    @NotNull(message = "Giá trị giảm không được để trống")
    @Positive(message = "Giá trị giảm phải lớn hơn 0")
    private BigDecimal giaTriGiam;

    private BigDecimal giaTriGiamToiDa;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private Instant ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private Instant ngayKetThuc;

    private String moTa;
}
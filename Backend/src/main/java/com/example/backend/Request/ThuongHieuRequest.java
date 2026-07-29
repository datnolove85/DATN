package com.example.backend.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ThuongHieuRequest {

    @NotBlank
    private String maThuongHieu;

    @NotBlank
    private String tenThuongHieu;

    private Boolean trangThai;
}
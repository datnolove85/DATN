package com.example.backend.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KichThuocRequest {

    @NotBlank
    private String maKichThuoc;

    @NotBlank
    private String tenKichThuoc;

    private Boolean trangThai;
}
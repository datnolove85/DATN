package com.example.backend.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MauSacRequest {

    @NotBlank
    private String maMauSac;

    @NotBlank
    private String tenMauSac;

    private Boolean trangThai;
}
package com.example.backend.Request;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamRequest {

    @NotNull
    private Integer idDanhMuc;

    @NotNull
    private Integer idThuongHieu;

    @NotNull
    private Integer idChatLieu;

    @NotBlank
    private String maSanPham;

    @NotBlank
    private String tenSanPham;

    private String moTa;
}
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

    private Integer idDanhMuc;


    private Integer idThuongHieu;


    private Integer idChatLieu;


    private String maSanPham;


    private String tenSanPham;

    private Boolean trangThai;

    private String moTa;
}
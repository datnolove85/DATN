package com.example.backend.Response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
public class SanPhamResponse {

    private Integer id;
    private String tenSanPham;

    private Integer idDanhMuc;
    private String tenDanhMuc;

    private String tenThuongHieu;
    private String tenChatLieu;

    private BigDecimal giaBan;

    private String image;
}
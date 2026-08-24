package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YeuThichResponseDTO {
    private Integer idSanPham;
    private String maSanPham;
    private String tenSanPham;
    private String tenThuongHieu;
    private String tenDanhMuc;
    private BigDecimal giaBan;
    private String hinhAnh;
    private LocalDateTime ngayTao;
}
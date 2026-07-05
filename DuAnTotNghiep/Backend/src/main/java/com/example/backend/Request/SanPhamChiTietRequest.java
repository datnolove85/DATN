package com.example.backend.Request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SanPhamChiTietRequest {

    private Integer idSanPham;

    private Integer idMauSac;

    private Integer idKichThuoc;

    private BigDecimal giaNhap;

    private BigDecimal giaBan;

    private Integer soLuongTon;

    private Boolean trangThai;
}
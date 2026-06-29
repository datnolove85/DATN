package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GioHangOnlineResponse {

    private Integer id;

    private Integer idSanPhamChiTiet;

    private String tenSanPham;

    private String maSanPhamChiTiet;

    private String mauSac;

    private String kichCo;

    private String anh;

    private BigDecimal giaBan;

    private Integer soLuong;

    private BigDecimal thanhTien;
}
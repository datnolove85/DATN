package com.example.backend.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiaChiKhachHangResponse {

    private Integer id;

    private String maDiaChi;

    private String tenNguoiNhan;

    private String soDienThoai;

    private String thanhPho;

    private String quan;

    private String phuong;

    private String diaChiCuThe;

    private Boolean macDinh;

    private Integer trangThai;

    private Integer idKhachHang;

    private String tenKhachHang;

    private Integer districtId;

    private String wardCode;

    private Double latitude;

    private Double longitude;

}
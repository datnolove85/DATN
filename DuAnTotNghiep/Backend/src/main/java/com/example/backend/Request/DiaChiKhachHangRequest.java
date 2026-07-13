package com.example.backend.Request;

import lombok.Data;

@Data
public class DiaChiKhachHangRequest {

    private String tenNguoiNhan;

    private String soDienThoai;

    private String thanhPho;

    private String quan;

    private String phuong;

    private String diaChiCuThe;

    private Boolean macDinh;
    private Integer districtId;

    private String wardCode;

    private Double latitude;

    private Double longitude;

}
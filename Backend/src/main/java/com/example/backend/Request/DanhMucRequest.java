package com.example.backend.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DanhMucRequest {

    private String maDanhMuc;
    private String tenDanhMuc;
    private Boolean trangThai;
}
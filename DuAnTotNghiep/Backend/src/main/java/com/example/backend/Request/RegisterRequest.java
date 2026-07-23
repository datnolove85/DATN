package com.example.backend.Request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String hoTen;
    private String tenTaiKhoan;
    private String email;
    private String soDienThoai;
    private String matKhau;
    private String xacNhanMatKhau;
}

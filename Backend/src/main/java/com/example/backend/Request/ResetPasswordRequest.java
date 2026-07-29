package com.example.backend.Request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String code;
    private String matKhauMoi;
    private String xacNhanMatKhau;
}

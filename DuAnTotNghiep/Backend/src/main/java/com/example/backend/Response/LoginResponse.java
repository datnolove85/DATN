package com.example.backend.Response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private String token;
    private Integer id;
    private Integer idNhanVien;
    private Integer idKhachHang;
    private String hoTen;
    private String email;
    private String vaiTro;


}
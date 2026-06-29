package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private Integer id;

    private String tenTaiKhoan;

    private String email;

    private String vaiTro;

}
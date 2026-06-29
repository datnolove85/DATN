package com.example.backend.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String identity; // email hoặc tên tài khoản

    private String password;

}
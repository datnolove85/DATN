package com.example.backend.Service;

import com.example.backend.Request.LoginRequest;
import com.example.backend.Response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}
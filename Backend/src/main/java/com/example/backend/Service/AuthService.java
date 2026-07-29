package com.example.backend.Service;

import com.example.backend.Request.LoginRequest;
import com.example.backend.Request.RegisterRequest;
import com.example.backend.Request.ForgotPasswordRequest;
import com.example.backend.Request.ResetPasswordRequest;
import com.example.backend.Response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void register(RegisterRequest request);

    void requestPasswordReset(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}
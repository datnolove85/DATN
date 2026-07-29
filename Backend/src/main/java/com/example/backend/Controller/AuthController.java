package com.example.backend.Controller;

import com.example.backend.Request.LoginRequest;

import com.example.backend.Request.ForgotPasswordRequest;
import com.example.backend.Request.RegisterRequest;
import com.example.backend.Request.ResetPasswordRequest;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import com.example.backend.Response.LoginResponse;
import com.example.backend.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.requestPasswordReset(request);
        return ResponseEntity.ok(Map.of("message", "Mã xác nhận đã được gửi"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(Map.of("message", "Đặt lại mật khẩu thành công"));
    }

}
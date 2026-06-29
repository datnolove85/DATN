package com.example.backend.Service.impl;

import com.example.backend.Entity.TaiKhoan;
import com.example.backend.Repository.TaiKhoanRepository;
import com.example.backend.Request.LoginRequest;
import com.example.backend.Response.LoginResponse;
import com.example.backend.Service.AuthService;
import com.example.backend.secutity.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {

        TaiKhoan tk = taiKhoanRepository
                .login(request.getIdentity())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (!tk.getMatKhau().equals(request.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtService.generateToken(tk);

        return new LoginResponse(
                token,
                tk.getId(),
                tk.getTenTaiKhoan(),
                tk.getEmail(),
                tk.getIdVaiTro().getTenVaiTro()
        );
    }
}
package com.example.backend.Service.impl;

import com.example.backend.Entity.KhachHang;
import com.example.backend.Entity.NhanVien;
import com.example.backend.Entity.TaiKhoan;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.Repository.NhanVienRepository;
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
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    @Override
    public LoginResponse login(LoginRequest request) {

        TaiKhoan tk = taiKhoanRepository
                .login(request.getIdentity())
                .orElseThrow(() ->
                        new RuntimeException("Tài khoản không tồn tại"));

        if (!tk.getMatKhau().equals(request.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtService.generateToken(tk);

        String role = tk.getIdVaiTro().getTenVaiTro();

        String hoTen = tk.getTenTaiKhoan(); // fallback
        Integer idNhanVien = null;
        Integer idKhachHang = null;

        if (role.equalsIgnoreCase("ADMIN") ||
                role.equalsIgnoreCase("STAFF")) {

            NhanVien nv = nhanVienRepository
                    .findByIdTaiKhoan_Id(tk.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

            idNhanVien = nv.getId();
            hoTen = nv.getTenNhanVien();
        }

        if (role.equalsIgnoreCase("USERS")) {

            KhachHang kh = khachHangRepository
                    .findByIdTaiKhoan_Id(tk.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

            idKhachHang = kh.getId();
            hoTen = kh.getHoTen();
        }

        switch (role.toUpperCase()) {

            case "ADMIN":
            case "STAFF":

                hoTen = nhanVienRepository
                        .findByIdTaiKhoan_Id(tk.getId())
                        .map(NhanVien::getTenNhanVien)
                        .orElse(tk.getTenTaiKhoan());

                break;

            case "USERS":

                hoTen = khachHangRepository
                        .findByIdTaiKhoan_Id(tk.getId())
                        .map(KhachHang::getHoTen)
                        .orElse(tk.getTenTaiKhoan());

                break;
        }

        return new LoginResponse(
                token,
                tk.getId(),
                idNhanVien,
                idKhachHang,
                hoTen,
                tk.getEmail(),
                role
        );
    }
}
package com.example.backend.Service.impl;

import com.example.backend.Entity.KhachHang;
import com.example.backend.Entity.NhanVien;
import com.example.backend.Entity.TaiKhoan;
import com.example.backend.Entity.VaiTro;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.Repository.NhanVienRepository;
import com.example.backend.Repository.TaiKhoanRepository;
import com.example.backend.Repository.VaiTroRepository;
import com.example.backend.Request.ForgotPasswordRequest;
import com.example.backend.Request.LoginRequest;
import com.example.backend.Request.RegisterRequest;
import com.example.backend.Request.ResetPasswordRequest;
import com.example.backend.Response.LoginResponse;
import com.example.backend.Service.AuthService;
import com.example.backend.secutity.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final JwtService jwtService;
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final VaiTroRepository vaiTroRepository;
    private final EmailImpl emailService;

    private final Map<String, ResetCode> resetCodes = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public LoginResponse login(LoginRequest request) {
        TaiKhoan tk = taiKhoanRepository.login(request.getIdentity().trim())
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        if (tk.getTrangThai() != null && tk.getTrangThai() == 0) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }
        if (!tk.getMatKhau().equals(request.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtService.generateToken(tk);
        String role = tk.getIdVaiTro().getTenVaiTro();
        String hoTen = tk.getTenTaiKhoan();
        Integer idNhanVien = null;
        Integer idKhachHang = null;

        if (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("STAFF")) {
            NhanVien nv = nhanVienRepository.findByIdTaiKhoan_Id(tk.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
            idNhanVien = nv.getId();
            hoTen = nv.getTenNhanVien();
        } else if (role.equalsIgnoreCase("USERS")) {
            KhachHang kh = khachHangRepository.findByIdTaiKhoan_Id(tk.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
            idKhachHang = kh.getId();
            hoTen = kh.getHoTen();
        }

        return new LoginResponse(token, tk.getId(), idNhanVien, idKhachHang, hoTen, tk.getEmail(), role);
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        String email = required(request.getEmail(), "Email").toLowerCase();
        String username = required(request.getTenTaiKhoan(), "Tên đăng nhập");
        String phone = required(request.getSoDienThoai(), "Số điện thoại");
        String fullName = required(request.getHoTen(), "Họ tên");
        String password = required(request.getMatKhau(), "Mật khẩu");

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new RuntimeException("Email không hợp lệ");
        if (!phone.matches("^[0-9]{9,11}$")) throw new RuntimeException("Số điện thoại phải gồm 9 đến 11 chữ số");
        if (password.length() < 6) throw new RuntimeException("Mật khẩu phải có ít nhất 6 ký tự");
        if (!password.equals(request.getXacNhanMatKhau())) throw new RuntimeException("Mật khẩu xác nhận không khớp");
        if (taiKhoanRepository.findByEmail(email).isPresent()) throw new RuntimeException("Email đã được sử dụng");
        if (taiKhoanRepository.findByTenTaiKhoan(username).isPresent()) throw new RuntimeException("Tên đăng nhập đã tồn tại");
        if (taiKhoanRepository.findBySoDienThoai(phone).isPresent()) throw new RuntimeException("Số điện thoại đã được sử dụng");

        VaiTro role = vaiTroRepository.findByTenVaiTro("USERS")
                .orElseThrow(() -> new RuntimeException("Chưa cấu hình vai trò USERS"));

        TaiKhoan account = new TaiKhoan();
        account.setIdVaiTro(role);
        account.setTenTaiKhoan(username);
        account.setEmail(email);
        account.setSoDienThoai(phone);
        account.setMatKhau(password);
        account.setNgayTao(Instant.now());
        account.setNguoiTao("SELF_REGISTER");
        account.setTrangThai(1);
        account = taiKhoanRepository.save(account);

        KhachHang customer = new KhachHang();
        customer.setIdTaiKhoan(account);
        customer.setMaKhachHang("KH" + String.format("%06d", account.getId()));
        customer.setHoTen(fullName);
        customer.setSoDienThoai(phone);
        customer.setHangThanhVien("THUONG");
        customer.setSoLanMua(0);
        customer.setNgayTao(Instant.now());
        customer.setNguoiTao("SELF_REGISTER");
        customer.setTrangThai(false);
        khachHangRepository.save(customer);
    }

    @Override
    public void requestPasswordReset(ForgotPasswordRequest request) {
        String email = required(request.getEmail(), "Email").toLowerCase();
        taiKhoanRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email chưa được đăng ký"));
        String code = String.format("%06d", secureRandom.nextInt(1_000_000));
        resetCodes.put(email, new ResetCode(code, Instant.now().plusSeconds(600)));
        emailService.sendPasswordResetCode(email, code);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String email = required(request.getEmail(), "Email").toLowerCase();
        String code = required(request.getCode(), "Mã xác nhận");
        String password = required(request.getMatKhauMoi(), "Mật khẩu mới");
        if (password.length() < 6) throw new RuntimeException("Mật khẩu phải có ít nhất 6 ký tự");
        if (!password.equals(request.getXacNhanMatKhau())) throw new RuntimeException("Mật khẩu xác nhận không khớp");

        ResetCode saved = resetCodes.get(email);
        if (saved == null || saved.expiresAt().isBefore(Instant.now()) || !saved.code().equals(code)) {
            throw new RuntimeException("Mã xác nhận không đúng hoặc đã hết hạn");
        }
        TaiKhoan account = taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email chưa được đăng ký"));
        account.setMatKhau(password);
        account.setNgayCapNhat(Instant.now());
        account.setNguoiCapNhat("PASSWORD_RESET");
        taiKhoanRepository.save(account);
        resetCodes.remove(email);
    }

    private String required(String value, String field) {
        if (value == null || value.trim().isEmpty()) throw new RuntimeException(field + " không được để trống");
        return value.trim();
    }

    private record ResetCode(String code, Instant expiresAt) {}
}

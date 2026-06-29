package com.example.backend.Controller;

import com.example.backend.Request.ThemGioHangRequest;
import com.example.backend.Service.GioHangService;
import com.example.backend.secutity.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/giohang")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GioHangController {

    private final GioHangService gioHangService;
    private final JwtService jwtService;

    // Thêm sản phẩm vào giỏ
    @PostMapping("/them")
    public ResponseEntity<?> them(
            @RequestBody ThemGioHangRequest request,
            HttpServletRequest servletRequest
    ) {

        String authHeader = servletRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Thiếu token");
        }

        String token = authHeader.substring(7);

        Integer idTaiKhoan = jwtService.extractId(token);

        gioHangService.themSanPham(idTaiKhoan, request);

        return ResponseEntity.ok("Thêm vào giỏ hàng thành công");
    }

    // Lấy giỏ hàng
    @GetMapping
    public ResponseEntity<?> layGioHang(HttpServletRequest servletRequest) {

        String authHeader = servletRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Thiếu token");
        }

        String token = authHeader.substring(7);

        Integer idTaiKhoan = jwtService.extractId(token);

        return ResponseEntity.ok(gioHangService.layGioHang(idTaiKhoan));
    }
}
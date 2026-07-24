package com.example.backend.Controller;


import com.example.backend.Service.DonHangService;
import com.example.backend.secutity.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/don-hang")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DonHangController {


    private final DonHangService donHangService;

    private final JwtService jwtService;



    // ==================================================
    // LẤY DANH SÁCH ĐƠN HÀNG CỦA KHÁCH HÀNG
    // ==================================================

    @GetMapping
    public ResponseEntity<?> layDanhSachDonHang(
            HttpServletRequest request
    ){

        String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")){

            return ResponseEntity
                    .badRequest()
                    .body("Thiếu token");

        }


        String token = authHeader.substring(7);


        Integer idTaiKhoan = jwtService.extractId(token);



        return ResponseEntity.ok(
                donHangService.layDanhSachDonHang(idTaiKhoan)
        );

    }

    // ==================================================
    // CHI TIẾT ĐƠN HÀNG
    // ==================================================

    // ==================================================
// CHI TIẾT ĐƠN HÀNG (DÙNG CHUNG CHO CẢ KHÁCH VÀ ADMIN)
// ==================================================
    @GetMapping("/{idHoaDon}")
    public ResponseEntity<?> chiTietDonHang(
            @PathVariable Integer idHoaDon,
            HttpServletRequest request
    ){
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity
                    .badRequest()
                    .body("Thiếu token");
        }

        String token = authHeader.substring(7);

        // 1. Lấy thông tin từ Token
        Integer idTaiKhoan = jwtService.extractId(token);
        String role = jwtService.extractRole(token); // <--- Lấy Role (ADMIN, STAFF, USER...)

        // 2. Kiểm tra nếu là ADMIN hoặc STAFF
        if ("ADMIN".equalsIgnoreCase(role) || "STAFF".equalsIgnoreCase(role)) {
            // Admin xem được MỌI đơn hàng, chỉ cần idHoaDon
            return ResponseEntity.ok(
                    donHangService.layChiTietDonHangChoAdmin(idHoaDon)
            );
        }

        // 3. Nếu là Khách hàng bình thường -> Chỉ xem được đơn của chính mình
        return ResponseEntity.ok(
                donHangService.layChiTietDonHang(idTaiKhoan, idHoaDon)
        );
    }

}
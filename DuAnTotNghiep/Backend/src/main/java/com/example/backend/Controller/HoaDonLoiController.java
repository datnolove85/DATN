package com.example.backend.Controller;

import com.example.backend.Repository.NhanVienRepository;
import com.example.backend.Request.HuyDonLoiRequest;
import com.example.backend.Request.HuyHangLoatLoiRequest;
import com.example.backend.Service.HoaDonLoiService;
import com.example.backend.secutity.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/don-hang-loi")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HoaDonLoiController {

    private final HoaDonLoiService hoaDonLoiService;

    private final JwtService jwtService;
    private final NhanVienRepository nhanVienRepo;


    // 1. Tìm đơn hàng có chứa sản phẩm bị lỗi
    @GetMapping("/tim-kiem")
    public ResponseEntity<?> timDonHangSanPhamLoi(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(hoaDonLoiService.timDonHangChuaGiaoChuaSanPhamLoi(keyword, page, size));
    }

    // 2. Hủy 1 đơn hàng lẻ do sản phẩm lỗi
    @PutMapping("/{id}/huy-loi")
    public ResponseEntity<?> huyDonLoiLe(
            @PathVariable Integer id,
            @RequestBody HuyDonLoiRequest request,
            HttpServletRequest servletRequest) {

        String token = servletRequest.getHeader("Authorization").substring(7);

        Integer idTaiKhoan = jwtService.extractId(token);

        Integer idNhanVien = nhanVienRepo
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"))
                .getId();

        hoaDonLoiService.huyDonLoiLe(id, request, idNhanVien);

        return ResponseEntity.ok(
                Map.of("message", "Đã hủy đơn hàng do sản phẩm lỗi thành công!")
        );
    }

    // 3. Hủy hàng loạt đơn hàng do sản phẩm lỗi
//    @PostMapping("/huy-hang-loat")
//    public ResponseEntity<?> huyHangLoatLoi(@RequestBody HuyHangLoatLoiRequest request) {
//        hoaDonLoiService.huyHangLoatLoi(request);
//        return ResponseEntity.ok(Map.of("message", "Đã hủy thành công các đơn hàng được chọn!"));
//    }
}
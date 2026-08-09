package com.example.backend.Controller;

import com.example.backend.Entity.DiemDanhHangNgay;
import com.example.backend.Entity.KhoVoucher;
import com.example.backend.Entity.PhanThuongMinigame;
import com.example.backend.Entity.VoucherCuaKhachHang;
import com.example.backend.Response.LichSuXuResponse;
import com.example.backend.Response.TrangThaiGamificationResponse;
import com.example.backend.Response.VoucherCuaKhachHangResponse;
import com.example.backend.Service.GamificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gamification")
@CrossOrigin(origins = "*")
public class GamificationController {

    @Autowired
    private GamificationService gamificationService;

    // API Điểm danh hàng ngày
    @PostMapping("/diem-danh/{idKhachHang}")
    public ResponseEntity<?> diemDanh(@PathVariable Integer idKhachHang) {
        try {
            DiemDanhHangNgay result = gamificationService.diemDanhHangNgay(idKhachHang);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API Quay thưởng Minigame (Truyền loại game: 'vong_quay' hoặc 'lat_the')
    @PostMapping("/quay-thuong")
    public ResponseEntity<?> quayThuong(@RequestParam Integer idKhachHang, @RequestParam String loaiGame) {
        try {
            PhanThuongMinigame reward = gamificationService.quayThuong(idKhachHang, loaiGame);
            return ResponseEntity.ok(reward);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API Lấy trạng thái ví xu & điểm danh của khách hàng
    @GetMapping("/trang-thai/{idKhachHang}")
    public ResponseEntity<?> getTrangThai(@PathVariable Integer idKhachHang) {
        try {
            TrangThaiGamificationResponse response = gamificationService.getTrangThaiGamification(idKhachHang);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API Lấy lịch sử biến động xu
    @GetMapping("/lich-su-xu/{idKhachHang}")
    public ResponseEntity<?> getLichSuXu(@PathVariable Integer idKhachHang) {
        try {
            List<LichSuXuResponse> response = gamificationService.getLichSuXu(idKhachHang);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

// --- CÁC API ĐỔI XU LẤY VOUCHER ---

    // 1. Lấy danh sách kho voucher có thể đổi
    @GetMapping("/kho-voucher")
    public ResponseEntity<?> getKhoVoucher() {
        try {
            List<KhoVoucher> list = gamificationService.getAllKhoVoucher();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Thực hiện đổi xu lấy voucher
    @PostMapping("/doi-voucher")
    public ResponseEntity<?> doiVoucher(@RequestParam Integer idKhachHang, @RequestParam Integer idKhoVoucher) {
        try {
            VoucherCuaKhachHang result = gamificationService.doiXuLayVoucher(idKhachHang, idKhoVoucher);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Lấy danh sách voucher cá nhân khách đã đổi
    @GetMapping("/voucher-cua-toi/{idKhachHang}")
    public ResponseEntity<?> getVoucherCuaToi(@PathVariable Integer idKhachHang) {
        try {
            List<VoucherCuaKhachHangResponse> list =
                    gamificationService.getVoucherCuaKhachHang(idKhachHang);

            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- CÁC API ADMIN: Quản lý số lượt lật thẻ ---

    // 1. Cập nhật số lượt lật thẻ cho 1 khách hàng cụ thể
    // URL: POST /api/gamification/admin/so-luot-lat-the/khach-hang/{idKhachHang}?soLuot=5

}
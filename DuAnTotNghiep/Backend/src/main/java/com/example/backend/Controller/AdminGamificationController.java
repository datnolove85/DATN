package com.example.backend.Controller;

import com.example.backend.Entity.KhoVoucher;
import com.example.backend.Entity.LichSuXu;
import com.example.backend.Entity.PhanThuongMinigame;
import com.example.backend.Service.GamificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/gamification")
@CrossOrigin(origins = "*")
public class AdminGamificationController {

    @Autowired
    private GamificationService gamificationService;

    // ==========================================
    // I. QUẢN LÝ KHO VOUCHER (ĐỔI XU)
    // ==========================================

    // 1. Lấy danh sách toàn bộ kho voucher
    @GetMapping("/kho-voucher")
    public ResponseEntity<List<KhoVoucher>> getAllKhoVoucher() {
        return ResponseEntity.ok(gamificationService.getAllKhoVoucher());
    }

    // 2. Thêm mới kho voucher
    @PostMapping("/kho-voucher")
    public ResponseEntity<?> createKhoVoucher(@RequestBody KhoVoucher khoVoucher) {
        try {
            KhoVoucher saved = gamificationService.createKhoVoucher(khoVoucher);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Sửa kho voucher
    @PutMapping("/kho-voucher/{id}")
    public ResponseEntity<?> updateKhoVoucher(@PathVariable Integer id, @RequestBody KhoVoucher khoVoucherMoi) {
        try {
            KhoVoucher updated = gamificationService.updateKhoVoucher(id, khoVoucherMoi);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. Xóa / Vô hiệu hóa kho voucher
    @DeleteMapping("/kho-voucher/{id}")
    public ResponseEntity<?> deleteKhoVoucher(@PathVariable Integer id) {
        try {
            gamificationService.deleteKhoVoucher(id);
            return ResponseEntity.ok("Xóa kho voucher thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // II. QUẢN LÝ PHẦN THƯỞNG MINIGAME
    // ==========================================

    // 5. Lấy danh sách toàn bộ cấu hình phần thưởng minigame
    @GetMapping("/phan-thuong")
    public ResponseEntity<List<PhanThuongMinigame>> getAllPhanThuong() {
        return ResponseEntity.ok(gamificationService.getAllPhanThuong());
    }

    // 6. Thêm mới phần thưởng minigame
    @PostMapping("/phan-thuong")
    public ResponseEntity<?> createPhanThuong(@RequestBody PhanThuongMinigame phanThuong) {
        try {
            PhanThuongMinigame saved = gamificationService.createPhanThuong(phanThuong);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 7. Sửa phần thưởng minigame
    @PutMapping("/phan-thuong/{id}")
    public ResponseEntity<?> updatePhanThuong(@PathVariable Integer id, @RequestBody PhanThuongMinigame phanThuongMoi) {
        try {
            PhanThuongMinigame updated = gamificationService.updatePhanThuong(id, phanThuongMoi);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 8. Xóa phần thưởng minigame
    @DeleteMapping("/phan-thuong/{id}")
    public ResponseEntity<?> deletePhanThuong(@PathVariable Integer id) {
        try {
            gamificationService.deletePhanThuong(id);
            return ResponseEntity.ok("Xóa phần thưởng thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==========================================
    // III. LỊCH SỬ HỆ THỐNG
    // ==========================================

    // 9. Xem toàn bộ lịch sử biến động xu của tất cả khách hàng
    @GetMapping("/lich-su-xu")
    public ResponseEntity<List<LichSuXu>> getAllLichSuXu() {
        return ResponseEntity.ok(gamificationService.getAllLichSuXuSystem());
    }
}
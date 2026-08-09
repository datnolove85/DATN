package com.example.backend.Controller;

import com.example.backend.Entity.CauHinhDiemDanh;
import com.example.backend.Service.CauHinhDiemDanhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/gamification")
@CrossOrigin(origins = "*") // Cho phép gọi API từ Frontend Vue.js
public class AdminCauHinhDiemDanhController {

    @Autowired
    private CauHinhDiemDanhService cauHinhDiemDanhService;

    // 1. Lấy danh sách toàn bộ cấu hình điểm danh
    @GetMapping("/cau-hinh-diem-danh")
    public ResponseEntity<List<CauHinhDiemDanh>> getAllCauHinh() {
        List<CauHinhDiemDanh> list = cauHinhDiemDanhService.getAllCauHinh();
        return ResponseEntity.ok(list);
    }

    // 2. Thêm mới một mốc ngày điểm danh đơn lẻ
    @PostMapping("/cau-hinh-diem-danh")
    public ResponseEntity<?> createCauHinh(@RequestBody CauHinhDiemDanh newConfig) {
        try {
            CauHinhDiemDanh saved = cauHinhDiemDanhService.createCauHinh(newConfig);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // 3. Cập nhật cấu hình điểm danh theo ID
    @PutMapping("/cau-hinh-diem-danh/{id}")
    public ResponseEntity<?> updateCauHinh(@PathVariable Integer id, @RequestBody CauHinhDiemDanh newInfo) {
        try {
            CauHinhDiemDanh updated = cauHinhDiemDanhService.updateCauHinh(id, newInfo);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // 4. Xóa mốc ngày điểm danh theo ID
    @DeleteMapping("/cau-hinh-diem-danh/{id}")
    public ResponseEntity<?> deleteCauHinh(@PathVariable Integer id) {
        try {
            cauHinhDiemDanhService.deleteCauHinh(id);
            return ResponseEntity.ok("Xóa cấu hình điểm danh thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // 5. API Sinh cấu hình tự động theo cấp số cộng (Hỗ trợ tạo nhanh chuỗi dài)
    @PostMapping("/cau-hinh-diem-danh/generate")
    public ResponseEntity<?> generateCauHinh(
            @RequestParam int totalDays,
            @RequestParam int initialValue,
            @RequestParam int step) {
        try {
            cauHinhDiemDanhService.generateCauHinhCapSoCong(totalDays, initialValue, step);
            return ResponseEntity.ok("Sinh cấu hình điểm danh theo cấp số cộng thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
}
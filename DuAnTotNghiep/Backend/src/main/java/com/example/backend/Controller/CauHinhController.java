package com.example.backend.Controller;

import com.example.backend.Entity.CauHinhHangThanhVien;
import com.example.backend.Entity.CauHinhHeThong;
import com.example.backend.Service.CauHinhService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/cau-hinh")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Đảm bảo FE gọi được API nếu chạy khác port
public class CauHinhController {

    private final CauHinhService cauHinhService;

    @GetMapping("/he-thong")
    public ResponseEntity<List<CauHinhHeThong>> getCauHinhHeThong() {
        return ResponseEntity.ok(cauHinhService.getAllCauHinhHeThong());
    }

    @PutMapping("/he-thong/{maCauHinh}")
    public ResponseEntity<?> updateCauHinhHeThong(
            @PathVariable String maCauHinh,
            @RequestParam(required = false) BigDecimal giaTriSo) {
        if (giaTriSo == null) return ResponseEntity.badRequest().body("Giá trị không hợp lệ");
        return ResponseEntity.ok(cauHinhService.updateCauHinhHeThong(maCauHinh, giaTriSo));
    }

    @GetMapping("/hang-thanh-vien")
    public ResponseEntity<List<CauHinhHangThanhVien>> getHangThanhVien() {
        return ResponseEntity.ok(cauHinhService.getAllHangThanhVien());
    }

    @PostMapping("/hang-thanh-vien")
    public ResponseEntity<CauHinhHangThanhVien> createHangThanhVien(@RequestBody CauHinhHangThanhVien entity) {
        return ResponseEntity.ok(cauHinhService.saveHangThanhVien(entity));
    }

    @PutMapping("/hang-thanh-vien/{id}")
    public ResponseEntity<CauHinhHangThanhVien> updateHangThanhVien(@PathVariable Integer id, @RequestBody CauHinhHangThanhVien entity) {
        return ResponseEntity.ok(cauHinhService.updateHangThanhVien(id, entity));
    }

    @DeleteMapping("/hang-thanh-vien/{id}")
    public ResponseEntity<String> deleteHangThanhVien(@PathVariable Integer id) {
        cauHinhService.deleteHangThanhVien(id);
        return ResponseEntity.ok("Xóa thành công!");
    }
}
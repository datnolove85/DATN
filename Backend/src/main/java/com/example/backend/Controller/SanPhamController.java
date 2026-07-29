package com.example.backend.Controller;

import com.example.backend.Entity.SanPham;
import com.example.backend.Request.SanPhamRequest;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.Service.SanPhamChiTietService;
import com.example.backend.Service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/sanpham")
public class SanPhamController {

    @Autowired
    SanPhamService sanPhamService;

    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @GetMapping
    public List<SanPhamResponse> getAllSanPham() {
        return sanPhamService.getAll();
    }

    // ================= CREATE =================
    @PostMapping
    public SanPham create(@RequestBody SanPhamRequest req) {
        return sanPhamService.create(req);
    }

    // ================= UPDATE =================
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody SanPhamRequest req) {
        try {
            SanPham updated = sanPhamService.update(id, req);

            // Tạo câu thông báo linh hoạt theo trạng thái truyền lên
            String message = Boolean.FALSE.equals(req.getTrangThai())
                    ? "Sản phẩm đã chuyển sang NGỪNG KINH DOANH và tự động gỡ khỏi các hóa đơn chờ!"
                    : "Cập nhật sản phẩm thành công!";

            return ResponseEntity.ok(Map.of(
                    "data", updated,
                    "message", message
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            sanPhamService.delete(id);

            return ResponseEntity.ok(Map.of(
                    "message", "Đã chuyển sản phẩm sang NGỪNG KINH DOANH và tự động gỡ khỏi các hóa đơn chờ!"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ================= GET BY ID =================
    @GetMapping("/detail/{id}")  // 🔥 FIX
    public SanPham getById(@PathVariable Integer id) {
        return sanPhamService.getById(id);
    }


}
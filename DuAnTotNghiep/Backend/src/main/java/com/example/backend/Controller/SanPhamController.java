package com.example.backend.Controller;

import com.example.backend.Entity.SanPham;
import com.example.backend.Request.SanPhamRequest;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.Service.SanPhamChiTietService;
import com.example.backend.Service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/sanpham")
public class SanPhamController {

    @Autowired
    SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    SanPhamService sanPhamService;

    @GetMapping()
    public List<SanPhamResponse> getAllSanPham() {
        return sanPhamChiTietService.getAllSanPham();
    }

    // ================= CREATE =================
    @PostMapping
    public SanPham create(@RequestBody SanPhamRequest req) {
        return sanPhamService.create(req);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public SanPham update(@PathVariable Integer id,
                          @RequestBody SanPhamRequest req) {
        return sanPhamService.update(id, req);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        sanPhamService.delete(id);
        return "Xóa sản phẩm thành công";
    }
    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public SanPham getById(@PathVariable Integer id) {
        return sanPhamService.getById(id);
    }
}

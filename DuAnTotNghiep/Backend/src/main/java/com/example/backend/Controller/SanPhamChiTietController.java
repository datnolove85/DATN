package com.example.backend.Controller;

import com.example.backend.Request.SanPhamChiTietRequest;
import com.example.backend.Response.SanPhamChiTietResponse;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.Service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/spct")
public class SanPhamChiTietController {

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;



    // ================= SPCT LIST (VARIANT MANAGE) =================
    @GetMapping()
    public ResponseEntity<List<SanPhamChiTietResponse>> getAllSpct() {
        return ResponseEntity.ok(
                sanPhamChiTietService.getAllSpct()
        );
    }

    // Trong SanPhamChiTietController.java

    @PostMapping
    public SanPhamChiTietResponse create(@ModelAttribute SanPhamChiTietRequest req) {
        return sanPhamChiTietService.add(req);
    }

    @PutMapping("/update/{id}")
    public SanPhamChiTietResponse update(
            @PathVariable Integer id,
            @ModelAttribute SanPhamChiTietRequest req) {
        return sanPhamChiTietService.update(id, req);
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {

        sanPhamChiTietService.delete(id);

        return "Xóa sản phẩm chi tiết thành công";
    }

    // ================= GET BY ID =================
    @GetMapping("/detail/{id}")
    public SanPhamChiTietResponse getById(
            @PathVariable Integer id) {

        return sanPhamChiTietService.getById(id);
    }

    @GetMapping("/sp/{id}")
    public List<SanPhamChiTietResponse> getBySpct(@PathVariable Integer id) {
        return  sanPhamChiTietService.getByIdSP(id);}

    }

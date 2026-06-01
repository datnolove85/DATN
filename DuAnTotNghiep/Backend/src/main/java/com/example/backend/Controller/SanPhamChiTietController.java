package com.example.backend.Controller;

import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Response.SanPhamChiTietResponse;
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
    SanPhamChiTietService sanPhamChiTietService;

    @GetMapping
    public List<SanPhamChiTietResponse> getAllSanPhamChiTiet() {
        return sanPhamChiTietService.getAllClient();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id){
        return ResponseEntity.ok(
                sanPhamChiTietService.getDetail(id)
        );
    }
}

package com.example.backend.Controller;

import com.example.backend.Request.SanPhamChiTietRequest;
import com.example.backend.Request.SanPhamCreateVariantRequest;
import com.example.backend.Response.SanPhamChiTietResponse;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.Service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SanPhamChiTietResponse update(
            @PathVariable Integer id,
            @ModelAttribute SanPhamChiTietRequest req,
            @RequestParam(value = "files", required = false) MultipartFile[] files) {

        return sanPhamChiTietService.update(id, req, files);
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            sanPhamChiTietService.delete(id);
            return ResponseEntity.ok("OK");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ================= GET BY ID =================
    @GetMapping("/detail/{id}")
    public SanPhamChiTietResponse getById(
            @PathVariable Integer id) {

        return sanPhamChiTietService.getById(id);
    }

    @GetMapping("/sp/{id}")
    public List<SanPhamChiTietResponse> getBySpct(@PathVariable Integer id) {
        return sanPhamChiTietService.getByIdSP(id);
    }


    @PostMapping("/bulk")
    public ResponseEntity<?> createBulk(@RequestBody List<SanPhamCreateVariantRequest> req) {
        sanPhamChiTietService.createBulk(req);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/sp/{id}/thong-ke-spct")
    public ResponseEntity<String> thongKe(@PathVariable Integer id) {
        return ResponseEntity.ok(sanPhamChiTietService.getThongKeSPCT(id));
    }

}



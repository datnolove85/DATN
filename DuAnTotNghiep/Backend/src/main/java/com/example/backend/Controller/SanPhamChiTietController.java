package com.example.backend.Controller;

import com.example.backend.Request.SanPhamChiTietRequest;
import com.example.backend.Request.SanPhamCreateVariantRequest;
import com.example.backend.Response.BulkVariantResponse;
import com.example.backend.Response.ProductVariantResponse;
import com.example.backend.Response.SanPhamChiTietResponse;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.Service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    // ================= UPDATE =================
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @ModelAttribute SanPhamChiTietRequest req,
            @RequestParam(value = "files", required = false) MultipartFile[] files) {

        // Service trả về Map<String, Object> chứa cả "data" (SPCT) và "message" (thông báo chi tiết)
        Map<String, Object> result = sanPhamChiTietService.update(id, req, files);

        return ResponseEntity.ok(result);
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            // Service giờ trả về Map<String, Object> (bao gồm cả "data" và "message")
            Map<String, Object> result = sanPhamChiTietService.delete(id);

            // Trả về trực tiếp result để FE nhận đủ cả { "data": {...}, "message": "..." }
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
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
    public ResponseEntity<List<BulkVariantResponse>> createBulk(
            @RequestBody List<SanPhamCreateVariantRequest> req) {

        List<BulkVariantResponse> result = sanPhamChiTietService.createBulk(req);

        return ResponseEntity.ok(result);
    }


    @GetMapping("/sp/{id}/thong-ke-spct")
    public ResponseEntity<String> thongKe(@PathVariable Integer id) {
        return ResponseEntity.ok(sanPhamChiTietService.getThongKeSPCT(id));
    }

    @GetMapping("/shop/{id}")
    public ResponseEntity<ProductVariantResponse> getVariantShop(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                sanPhamChiTietService.getVariantForShop(id)
        );
    }
}



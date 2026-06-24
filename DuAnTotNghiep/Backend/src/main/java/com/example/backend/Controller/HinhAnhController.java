package com.example.backend.Controller;

import com.example.backend.Entity.HinhAnh;
import com.example.backend.Response.HinhAnhResponse;
import com.example.backend.Service.HinhAnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/hinhanh")
public class HinhAnhController {

    @Autowired
    HinhAnhService hinhanhService;

    @GetMapping
    public List<HinhAnh> getHinhanh() {
        return hinhanhService.getHinhAnh();
    }

    @PostMapping("/upload/{idSpct}")
    public ResponseEntity<?> upload(
            @PathVariable Integer idSpct,
            @RequestParam("files") MultipartFile[] files
    ) throws IOException {
        hinhanhService.upload(idSpct, files);
        return ResponseEntity.ok("Thêm ảnh thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id) {
        hinhanhService.delete(id);
        return ResponseEntity.ok("Xóa ảnh thành công");
    }

    @GetMapping("/spct/{id}")
    public ResponseEntity<List<HinhAnhResponse>> getBySPCT(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                hinhanhService.getBySPCT(id)
        );
    }
}

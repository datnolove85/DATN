    package com.example.backend.Controller;

    import com.example.backend.Entity.DotGiamGia;
    import com.example.backend.Repository.DotGiamGiaRepository;
    import com.example.backend.Request.CreateDotGiamGiaRequest;
    import com.example.backend.Request.DotGiamGiaRequest;
    import com.example.backend.Request.ThemSanPhamGGRequest;
    import com.example.backend.Request.UpdateDotGiamGiaRequest;
    import com.example.backend.Response.DotGiamGiaResponse;
    import com.example.backend.Response.SanPhamGiamGiaResponse;
    import com.example.backend.Service.DotGiamGiaService;
    import com.example.backend.Service.impl.DotGiamGiaServiceImpl;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Map;

    @RestController
    @RequestMapping("/dot-giam-gia")
    @RequiredArgsConstructor
    @CrossOrigin("*")
    public class DotGiamGiaController {

        private final DotGiamGiaService service;

        //====================================
        // Danh sách
        //====================================

        @GetMapping
        public ResponseEntity<?> getAll() {

            return ResponseEntity.ok(
                    service.getAll()
            );

        }


        //====================================
        // Chi tiết
        //====================================

        @GetMapping("/{id}")
        public ResponseEntity<?> getById(
                @PathVariable Integer id
        ) {

            return ResponseEntity.ok(
                    service.getById(id)
            );

        }

        //====================================
        // Tạo
        //====================================

        @PostMapping
        public ResponseEntity<?> create(
                @RequestBody CreateDotGiamGiaRequest request
        ) {

            return ResponseEntity.ok(
                    service.create(request)
            );

        }

        //====================================
        // Cập nhật
        //====================================

        @PutMapping("/{id}")
        public ResponseEntity<?> update(
                @PathVariable Integer id,
                @RequestBody UpdateDotGiamGiaRequest request
        ) {

            return ResponseEntity.ok(
                    service.update(id, request)
            );

        }

        //====================================
        // Xóa
        //====================================

        @DeleteMapping("/{id}")
        public ResponseEntity<?> delete(
                @PathVariable Integer id
        ) {

            service.delete(id);

            return ResponseEntity.ok("Xóa thành công");

        }

        //====================================
        // Đổi trạng thái
        //====================================

        @PutMapping("/{id}/doi-trang-thai")
        public ResponseEntity<?> doiTrangThai(
                @PathVariable Integer id
        ) {

            service.doiTrangThai(id);

            return ResponseEntity.ok("Cập nhật thành công");

        }

        //====================================
        // Thêm sản phẩm
        //====================================

        @PostMapping("/{id}/san-pham")
        public ResponseEntity<?> themSanPham(
                @PathVariable Integer id,
                @RequestBody ThemSanPhamGGRequest request
        ) {

            return ResponseEntity.ok(
                    service.themSanPham(id, request)
            );

        }

        //====================================
        // Xóa sản phẩm
        //====================================

        @DeleteMapping("/{id}/san-pham/{idSPCT}")
        public ResponseEntity<?> xoaSanPham(
                @PathVariable Integer id,
                @PathVariable Integer idSPCT
        ) {

            service.xoaSanPham(id, idSPCT);

            return ResponseEntity.ok(
                    Map.of("message", "Đã xóa")
            );

        }

        //====================================
        // Danh sách sản phẩm
        //====================================

        @GetMapping("/{id}/san-pham")
        public ResponseEntity<?> getSanPham(
                @PathVariable Integer id
        ) {

            return ResponseEntity.ok(
                    service.getSanPham(id)
            );

        }

        //====================================
        // Danh sách sản phẩm chưa áp dụng
        //====================================

        @GetMapping("/{id}/san-pham-chua-ap-dung")
        public ResponseEntity<?> getSanPhamChuaApDung(
                @PathVariable Integer id
        ) {

            return ResponseEntity.ok(
                    service.getSanPhamChuaApDung(id)
            );

        }





    }
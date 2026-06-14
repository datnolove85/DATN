package com.example.backend.Controller;

import com.example.backend.Request.*;
import com.example.backend.Response.HoaDonResponse;
import com.example.backend.Service.HoaDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/hoadon")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService service;

    // ================= GET ALL =================
    @GetMapping
    public List<HoaDonResponse> getAll() {
        return service.getAll();
    }

    @PostMapping("/thanh-toan")
    public ResponseEntity<?> thanhToan(
            @RequestBody ThanhToanHoaDonRequest req
    ) {

        return ResponseEntity.ok(
                service.thanhToanHoaDon(req)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> detail(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                service.getDetail(id)
        );
    }

    //Mới
    @GetMapping("/hoa-don-cho")
    public ResponseEntity<?> getHoaDonCho() {

        return ResponseEntity.ok(
                service.getHoaDonCho()
        );
    }

    @PostMapping("/hoa-don-cho")
    public ResponseEntity<?> taoHoaDonCho() {

        return ResponseEntity.ok(
                service.taoHoaDonCho()
        );
    }


    @GetMapping("/{id}/chi-tiet")
    public ResponseEntity<?> getChiTietHoaDon(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                service.getChiTietHoaDon(id)
        );
    }

    @PutMapping("/chi-tiet/{id}/giam")
    public ResponseEntity<?> giamSoLuong(
            @PathVariable Integer id
    ) {

        service.giamSoLuong(id);

        return ResponseEntity.ok("OK");
    }

    @PostMapping("/them-san-pham")
    public ResponseEntity<?> themSanPham(
            @RequestBody ThemSanPhamRequest request
    ) {
        service.themSanPhamVaoHoaDon(
                request
        );

        return ResponseEntity.ok(
                "Thêm sản phẩm thành công"
        );
    }

    @PutMapping("/chi-tiet/{id}/tang")
    public ResponseEntity<?> tangSoLuong(
            @PathVariable Integer id
    ) {

        service.tangSoLuong(id);

        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/chi-tiet/{id}")
    public ResponseEntity<?> xoaSanPhamKhoiHoaDon(
            @PathVariable Integer id
    ) {

        service.xoaSanPhamKhoiHoaDon(id);

        return ResponseEntity.ok("OK");
    }

    @PutMapping("/gan-khach-hang")
    public ResponseEntity<?> ganKhachHang(
            @RequestBody GanKhachHangRequest request
    ) {

        service.ganKhachHang(
                request.getIdHoaDon(),
                request.getIdKhachHang()
        );

        return ResponseEntity.ok("Gán khách hàng thành công");
    }

    @PutMapping("/huy/{id}")
    public ResponseEntity<?> huyHoaDon(@PathVariable Integer id) {
        service.huyHoaDon(id);
        return ResponseEntity.ok("Hủy hóa đơn thành công");
    }

}
package com.example.backend.Controller;

import com.example.backend.Request.HoaDonRequest;
import com.example.backend.Request.TaoHoaDonRequest;
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

    @PostMapping("/ban-hang")
    public ResponseEntity<?> banHang(@RequestBody TaoHoaDonRequest req) {
        return ResponseEntity.ok(service.banHang(req));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> detail(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                service.getDetail(id)
        );
    }


}
package com.example.backend.Controller;

import com.example.backend.Response.HoaDonResponse;
import com.example.backend.Service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hoa-don")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping
    public ResponseEntity<List<HoaDonResponse>> getAllHoaDon() {
        List<HoaDonResponse> list = hoaDonService.getAllHoaDon();
        return ResponseEntity.ok(list);
    }
}

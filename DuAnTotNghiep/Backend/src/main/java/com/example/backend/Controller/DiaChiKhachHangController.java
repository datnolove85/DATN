package com.example.backend.Controller;

import com.example.backend.Service.DiaChiKhachHangService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kh/dia-chi")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DiaChiKhachHangController {

    private final DiaChiKhachHangService diaChiKhachHangService;

    @GetMapping()
    public ResponseEntity<?> getDiaChiMacDinh(HttpServletRequest request) {

        return ResponseEntity.ok(
                diaChiKhachHangService.getDiaChiMacDinh(request)
        );
    }

}
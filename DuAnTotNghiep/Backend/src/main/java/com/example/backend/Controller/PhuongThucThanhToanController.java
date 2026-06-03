package com.example.backend.Controller;

import com.example.backend.Entity.PhuongThucThanhToan;
import com.example.backend.Service.PhuongThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/pttt")
public class PhuongThucThanhToanController {

    @Autowired
    PhuongThucThanhToanService phuongThucThanhToanService;

    @GetMapping
    public List<PhuongThucThanhToan> getPhuongThucThanhToan() {
        return phuongThucThanhToanService.getAllPhuongThucThanhToan();
    }
}

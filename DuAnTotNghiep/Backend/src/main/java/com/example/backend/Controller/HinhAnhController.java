package com.example.backend.Controller;

import com.example.backend.Entity.HinhAnh;
import com.example.backend.Service.HinhAnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

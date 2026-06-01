package com.example.backend.Controller;

import com.example.backend.Entity.DanhMuc;
import com.example.backend.Service.DanhMucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/danhmuc")
public class DanhMucController {

    @Autowired
    DanhMucService danhMucService;

    @GetMapping
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucService.getAllDanhMuc();
    }

}

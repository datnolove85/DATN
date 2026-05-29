package com.example.backend.Controller;

import com.example.backend.Entity.SanPham;
import com.example.backend.Service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/sanpham")
public class SanPhamController {

    @Autowired
    SanPhamService sanPhamService;

   @GetMapping
    public List<SanPham> getAll(){
       return sanPhamService.getAllSanPham();
   }


}

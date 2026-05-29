package com.example.backend.Service;

import com.example.backend.Entity.SanPham;
import com.example.backend.Repository.SanPhamrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamService {
    @Autowired
    SanPhamrepository sanPhamrepository;

    public List<SanPham> getAllSanPham(){
        return sanPhamrepository.findAll();
    }
}

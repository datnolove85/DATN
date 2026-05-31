package com.example.backend.Service;

import com.example.backend.Entity.HinhAnh;
import com.example.backend.Repository.HinhAnhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HinhAnhService {

    @Autowired
    HinhAnhRepository HinhAnhRepository;

    public List<HinhAnh> getHinhAnh(){
        return HinhAnhRepository.findAll();
    }
}

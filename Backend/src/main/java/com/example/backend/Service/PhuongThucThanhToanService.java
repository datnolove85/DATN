package com.example.backend.Service;

import com.example.backend.Entity.PhuongThucThanhToan;
import com.example.backend.Repository.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhuongThucThanhToanService {

    @Autowired
    PhuongThucThanhToanRepository  phuongThucThanhToanRepository;

    public List<PhuongThucThanhToan> getAllPhuongThucThanhToan() {
        return phuongThucThanhToanRepository.findAll();
    }
}

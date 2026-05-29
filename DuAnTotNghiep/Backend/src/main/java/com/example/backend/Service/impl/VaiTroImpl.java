package com.example.backend.Service.impl;

import com.example.backend.Repository.VaiTroRepository;
import com.example.backend.Response.VaiTroResponse;
import com.example.backend.Service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaiTroImpl implements VaiTroService {
    @Autowired
    VaiTroRepository vaiTroRepository;
    @Override
    public Page<VaiTroResponse> phanTrangVaiTro(Integer pageNo, Integer pageSize) {
        return null;
    }

    @Override
    public List<VaiTroResponse> getAllVaiTro() {
        return vaiTroRepository.hienThi();
    }
}

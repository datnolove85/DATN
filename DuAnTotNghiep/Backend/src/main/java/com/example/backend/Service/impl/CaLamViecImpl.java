package com.example.backend.Service.impl;

import com.example.backend.Entity.CaLamViec;
import com.example.backend.Repository.CaLamViecRepository;
import com.example.backend.Request.CaLamViecRequest;
import com.example.backend.Response.CaLamViecResponse;
import com.example.backend.Service.CaLamViecService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CaLamViecImpl implements CaLamViecService {
    @Autowired
    CaLamViecRepository caLamViecRepository;

    @Override
    public Page<CaLamViecResponse> phanTrangCaLamViec(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo , pageSize);
        return caLamViecRepository.phanTrang(pageable);
    }

    @Override
    public void deleleteCaLamViec(Integer id) {
        CaLamViec caLamViec = caLamViecRepository.findById(id).get();
        caLamViec.setTrangThai(0);
        caLamViecRepository.save(caLamViec);

    }

    @Override
    public void addCaLamViec(CaLamViecRequest caLamViecRequest) {
         CaLamViec caLamViec = new CaLamViec();
         BeanUtils.copyProperties(caLamViecRequest, caLamViec);
         caLamViecRepository.save(caLamViec);
    }

    @Override
    public void updateCaLamViec(Integer id, CaLamViecRequest caLamViecRequest) {
        CaLamViec caLamViec = caLamViecRepository.findById(id).get();
        BeanUtils.copyProperties(caLamViecRequest, caLamViec);
        caLamViecRepository.save(caLamViec);
    }

    @Override
    public CaLamViecResponse detailCaLamViec(Integer id) {
        return caLamViecRepository.detail(id);
    }
}

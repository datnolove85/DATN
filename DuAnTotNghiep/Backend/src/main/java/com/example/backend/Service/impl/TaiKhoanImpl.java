package com.example.backend.Service.impl;

import com.example.backend.Response.TaiKhoanResponse;
import com.example.backend.Service.TaiKhoanService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TaiKhoanImpl implements TaiKhoanService {
    @Override
    public Page<TaiKhoanResponse> phanTrangTaiKhoan(Integer pageNo, Integer pageSize) {
        return null;
    }
}

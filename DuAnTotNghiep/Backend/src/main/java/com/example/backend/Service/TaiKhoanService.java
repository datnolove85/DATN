package com.example.backend.Service;

import com.example.backend.Response.TaiKhoanResponse;
import org.springframework.data.domain.Page;

public interface TaiKhoanService {
       Page<TaiKhoanResponse> phanTrangTaiKhoan(Integer pageNo, Integer pageSize);

}

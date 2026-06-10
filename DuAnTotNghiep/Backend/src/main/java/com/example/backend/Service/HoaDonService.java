package com.example.backend.Service;


import com.example.backend.Request.HoaDonRequest;
import com.example.backend.Request.TaoHoaDonRequest;
import com.example.backend.Response.HoaDonDetailResponse;
import com.example.backend.Response.HoaDonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HoaDonService {
    Object banHang(TaoHoaDonRequest req);

    List<HoaDonResponse> filter(String trangThai, String loaiHoaDon);

    List<HoaDonResponse> search(String keyword);

    Page<HoaDonResponse> getAll(Pageable pageable);

    List<HoaDonResponse> getAll();

    HoaDonDetailResponse getDetail(Integer id);
}
package com.example.backend.Service;

import com.example.backend.Request.HuyDonLoiRequest;
import com.example.backend.Request.HuyHangLoatLoiRequest;
import com.example.backend.Entity.HoaDon;
import org.springframework.data.domain.Page;

public interface HoaDonLoiService {
    Page<HoaDon> timDonHangChuaGiaoChuaSanPhamLoi(String keyword, int page, int size);
    void huyDonLoiLe(Integer hoaDonId, HuyDonLoiRequest request);
    void huyHangLoatLoi(HuyHangLoatLoiRequest request);
}
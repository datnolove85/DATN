package com.example.backend.Service;

import com.example.backend.Request.TraHangRequest;
import com.example.backend.Response.TraHangResponse;

public interface TraHangService {
    TraHangResponse getThongTinTraHang(Integer hoaDonId);

    void traHang(TraHangRequest request);
}

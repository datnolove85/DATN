package com.example.backend.Service;

import com.example.backend.Request.ThemGioHangRequest;
import com.example.backend.Response.GioHangOnlineResponse;
import com.example.backend.Response.GioHangResponse;

import java.util.List;

public interface GioHangService {

    void themSanPham(Integer idTaiKhoan, ThemGioHangRequest request);

    List<GioHangOnlineResponse> layGioHang(Integer idTaiKhoan);

    void xoaSanPham(Integer idTaiKhoan, Integer idGioHangChiTiet);
}
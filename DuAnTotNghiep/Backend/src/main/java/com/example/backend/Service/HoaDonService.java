package com.example.backend.Service;


import com.example.backend.Entity.HoaDon;
import com.example.backend.Request.HoaDonRequest;
import com.example.backend.Request.TaoHoaDonRequest;
import com.example.backend.Request.ThanhToanHoaDonRequest;
import com.example.backend.Request.ThemSanPhamRequest;
import com.example.backend.Response.GioHangResponse;
import com.example.backend.Response.HoaDonDetailResponse;
import com.example.backend.Response.HoaDonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HoaDonService {

    List<HoaDonResponse> filter(String trangThai, String loaiHoaDon);

    List<HoaDonResponse> search(String keyword);

    Page<HoaDonResponse> getAll(Pageable pageable);

    List<HoaDonResponse> getAll();

    HoaDonDetailResponse getDetail(Integer id);

    HoaDon taoHoaDonCho();

    List<HoaDon> getHoaDonCho();

    List<GioHangResponse> getChiTietHoaDon(
            Integer idHoaDon
    );

    void themSanPhamVaoHoaDon(
            ThemSanPhamRequest request
    );

    void xoaSanPhamKhoiHoaDon(Integer idHoaDonChiTiet);

    void tangSoLuong(Integer idHoaDonChiTiet);

    void giamSoLuong(Integer idHoaDonChiTiet);

    void ganKhachHang(Integer idHoaDon, Integer idKhachHang);

    @Transactional
    public Object thanhToanHoaDon(
            ThanhToanHoaDonRequest req
    );

    void huyHoaDon(Integer id);

}
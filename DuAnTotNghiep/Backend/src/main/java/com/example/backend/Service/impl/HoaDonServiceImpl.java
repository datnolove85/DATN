package com.example.backend.Service.impl;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Response.HoaDonResponse;
import com.example.backend.Service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Override
    public List<HoaDonResponse> getAllHoaDon() {
        List<HoaDon> listEntity = hoaDonRepository.findAll();

        return listEntity.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private HoaDonResponse convertToResponse(HoaDon entity) {
        HoaDonResponse response = new HoaDonResponse();
        response.setId(entity.getId());

        // Tránh NullPointerException nếu hóa đơn chưa có khách hàng gắn vào
        if (entity.getIdKhachHang() != null) {
            response.setIdKhachHang(entity.getIdKhachHang().getId());
        }

        response.setMaHoaDon(entity.getMaHoaDon());
        response.setTongTienHang(entity.getTongTienHang());
        response.setTongGiamGia(entity.getTongGiamGia());
        response.setPhiVanChuyen(entity.getPhiVanChuyen());
        response.setTongThanhToan(entity.getTongThanhToan());
        response.setTenNguoiNhan(entity.getTenNguoiNhan());
        response.setSoDienThoaiNguoiNhan(entity.getSoDienThoaiNguoiNhan());
        response.setDiaChiGiaoHang(entity.getDiaChiGiaoHang());
        response.setLoaiHoaDon(entity.getLoaiHoaDon());
        response.setTrangThai(entity.getTrangThai());
        response.setGhiChu(entity.getGhiChu());
        response.setNgayTao(entity.getNgayTao());
        response.setNgayCapNhat(entity.getNgayCapNhat());
        return response;
    }
}

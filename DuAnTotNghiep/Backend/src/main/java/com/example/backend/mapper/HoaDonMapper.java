package com.example.backend.mapper;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Request.HoaDonRequest;
import com.example.backend.Response.HoaDonResponse;

import java.math.BigDecimal;


public class HoaDonMapper {

    public static HoaDon toEntity(HoaDonRequest req) {
        HoaDon hd = new HoaDon();

        hd.setMaHoaDon(req.getMaHoaDon());
        hd.setTongTienHang(req.getTongTienHang());
        hd.setTongGiamGia(req.getTongGiamGia());
        hd.setPhiVanChuyen(req.getPhiVanChuyen());
        hd.setTongThanhToan(req.getTongThanhToan());
        hd.setTenNguoiNhan(req.getTenNguoiNhan());
        hd.setSoDienThoaiNguoiNhan(req.getSoDienThoaiNguoiNhan());
        hd.setDiaChiGiaoHang(req.getDiaChiGiaoHang());
        hd.setLoaiHoaDon(req.getLoaiHoaDon());
        hd.setTrangThai(req.getTrangThai());
        hd.setGhiChu(req.getGhiChu());

        return hd;
    }

    public static HoaDonResponse toResponse(HoaDon hd) {
        HoaDonResponse res = new HoaDonResponse();

        res.setId(hd.getId());
        res.setIdKhachHang(
                hd.getIdKhachHang() != null ? hd.getIdKhachHang().getId() : null
        );

        res.setMaHoaDon(hd.getMaHoaDon());
        res.setTongTienHang(hd.getTongTienHang());
        res.setTongGiamGia(hd.getTongGiamGia());
        res.setPhiVanChuyen(hd.getPhiVanChuyen());
        res.setTongThanhToan(hd.getTongThanhToan());
        res.setTenNguoiNhan(hd.getTenNguoiNhan());
        res.setSoDienThoaiNguoiNhan(hd.getSoDienThoaiNguoiNhan());
        res.setDiaChiGiaoHang(hd.getDiaChiGiaoHang());
        res.setLoaiHoaDon(hd.getLoaiHoaDon());
        res.setTrangThai(hd.getTrangThai());
        res.setGhiChu(hd.getGhiChu());
        res.setNgayTao(hd.getNgayTao());
        res.setNgayCapNhat(hd.getNgayCapNhat());

        return res;
    }
    public static HoaDonResponse fromViewRow(Object[] row) {

        HoaDonResponse res = new HoaDonResponse();

        res.setId((Integer) row[0]);
        res.setMaHoaDon((String) row[1]);
        res.setTenNguoiNhan((String) row[2]);
        res.setSoDienThoaiNguoiNhan((String) row[3]);
        res.setLoaiHoaDon((String) row[4]);
        res.setTrangThai((String) row[5]);

        // DATE → Instant
        res.setNgayTao(((java.sql.Timestamp) row[6]).toInstant());

        // MONEY → BigDecimal (CHUẨN NHẤT)
        res.setTongTienHang((BigDecimal) row[7]);
        res.setTongGiamGia((BigDecimal) row[8]);
        res.setTongThanhToan((BigDecimal) row[9]);

        res.setPhuongThucThanhToan((String) row[10]);

        return res;
    }
}
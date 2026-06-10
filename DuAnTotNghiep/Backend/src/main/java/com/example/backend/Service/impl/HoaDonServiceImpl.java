package com.example.backend.Service.impl;

import com.example.backend.Entity.*;

import com.example.backend.Repository.*;
import com.example.backend.Request.ChiTietMua;
import com.example.backend.Request.HoaDonRequest;
import com.example.backend.Request.TaoHoaDonRequest;
import com.example.backend.Response.HoaDonChiTietResponse;
import com.example.backend.Response.HoaDonDetailResponse;
import com.example.backend.Response.HoaDonResponse;
import com.example.backend.Service.HoaDonService;
import com.example.backend.mapper.HoaDonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonRepository hoaDonRepo;
    private final HoaDonChiTietRepository ctRepo;
    private final SanPhamChiTietRepository spctRepo;
    private final ThanhToanRepository ttRepo;
    private final PhuongThucThanhToanRepository ptRepo;
    private final KhachHangRepository khRepo;



    @Transactional
    public Object banHang(TaoHoaDonRequest req) {

        // ===================== 1. HÓA ĐƠN =====================

        Integer idKH = req.getIdKhachHang();
        if(idKH == null){
            idKH=6;
        }

        KhachHang kh = khRepo.findById(idKH).orElse(null);

        HoaDon hd = new HoaDon();
        Long count = hoaDonRepo.count() + 1;
        hd.setMaHoaDon(String.format("HD%05d", count));
        hd.setIdKhachHang(kh);
        hd.setTenNguoiNhan(kh.getHoTen());
        hd.setSoDienThoaiNguoiNhan(kh.getSoDienThoai());
        hd.setLoaiHoaDon(req.getLoaiHoaDon());
        hd.setDiaChiGiaoHang(kh.getDiaChi());
        hd.setTrangThai("da_giao");

        hoaDonRepo.save(hd);

        // ===================== 2. TÍNH TIỀN =====================
        BigDecimal tongTien = BigDecimal.ZERO;

        // ===================== 3. CHI TIẾT =====================
        for (ChiTietMua item : req.getChiTietMua()) {

            SanPhamChiTiet spct = spctRepo.findById(item.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy SP"));

            if (spct.getSoLuongTon() < item.getSoLuong()) {
                throw new RuntimeException("Không đủ tồn kho");
            }

            BigDecimal thanhTien = item.getDonGia()
                    .multiply(BigDecimal.valueOf(item.getSoLuong()));

            HoaDonChiTiet ct = new HoaDonChiTiet();
            ct.setIdHoaDon(hd);
            ct.setIdSanPhamChiTiet(spct);
            ct.setSoLuong(item.getSoLuong());
            ct.setDonGia(item.getDonGia());
            ct.setThanhTien(thanhTien);

            ctRepo.save(ct);

            tongTien = tongTien.add(thanhTien);

            // trừ kho
            spct.setSoLuongTon(spct.getSoLuongTon() - item.getSoLuong());
            spctRepo.save(spct);
        }

        // ===================== 4. THANH TOÁN =====================
        PhuongThucThanhToan pt = ptRepo.findById(req.getPhuongThucTt())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phương thức"));

        ThanhToan tt = new ThanhToan();
        tt.setIdHoaDon(hd);
        tt.setIdPhuongThucThanhToan(pt);
        tt.setSoTien(tongTien);
        tt.setTrangThai("da_thanh_toan");
        tt.setNgayThanhToan(Instant.now());

        ttRepo.save(tt);

        // ===================== 5. UPDATE HÓA ĐƠN =====================
        hd.setTongTienHang(tongTien);
        hd.setTongGiamGia(BigDecimal.ZERO);
        hd.setTongThanhToan(tongTien);
        hd.setNgayCapNhat(Instant.now());

        hoaDonRepo.save(hd);

        // ===================== 6. RESPONSE =====================
        Map<String, Object> res = new HashMap<>();
        res.put("id", hd.getId());
        res.put("maHoaDon", hd.getMaHoaDon());
        res.put("tongTienHang", tongTien);
        res.put("tongThanhToan", tongTien);
        res.put("trangThai", hd.getTrangThai());

        return res;
    }

    @Override
    public List<HoaDonResponse> getAll() {
        return hoaDonRepo.getAllResponse();
    }

    // ================= FILTER =================
    @Override
    public List<HoaDonResponse> filter(String trangThai, String loaiHoaDon) {
        return hoaDonRepo.getAllFromView()
                .stream()
                .map(HoaDonMapper::fromViewRow)
                .filter(x ->
                        (trangThai == null || x.getTrangThai().equals(trangThai)) &&
                                (loaiHoaDon == null || x.getLoaiHoaDon().equals(loaiHoaDon))
                )
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================
    @Override
    public List<HoaDonResponse> search(String keyword) {
        return hoaDonRepo.getAllFromView()
                .stream()
                .map(HoaDonMapper::fromViewRow)
                .filter(x ->
                        x.getMaHoaDon().contains(keyword) ||
                                x.getTenNguoiNhan().contains(keyword) ||
                                x.getSoDienThoaiNguoiNhan().contains(keyword)
                )
                .collect(Collectors.toList());
    }

    // ================= PAGINATION =================
    @Override
    public Page<HoaDonResponse> getAll(Pageable pageable) {

        List<HoaDonResponse> all = hoaDonRepo.getAllFromView()
                .stream()
                .map(HoaDonMapper::fromViewRow)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());

        List<HoaDonResponse> pageContent = all.subList(start, end);

        return new PageImpl<>(pageContent, pageable, all.size());
    }



    public HoaDonDetailResponse getDetail(Integer id) {

        HoaDon hd = hoaDonRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hóa đơn"));

        List<HoaDonChiTietResponse> sanPhams =
                ctRepo.findByIdHoaDon_Id(id)
                        .stream()
                        .map(ct -> {

                            SanPhamChiTiet spct =
                                    ct.getIdSanPhamChiTiet();

                            return new HoaDonChiTietResponse(
                                    ct.getId(),
                                    spct.getId(),
                                    spct.getMaSanPhamChiTiet(),
                                    spct.getTenSanPhamChiTiet(),
                                    spct.getIdMauSac().getTenMauSac(),
                                    spct.getIdKichThuoc().getTenKichThuoc(),
                                    ct.getSoLuong(),
                                    ct.getDonGia(),
                                    ct.getThanhTien()
                            );
                        })
                        .toList();

        String phuongThuc = "";

        Optional<ThanhToan> thanhToan =
                ttRepo.findFirstByIdHoaDon_Id(id);

        if (thanhToan.isPresent()) {

            phuongThuc =
                    thanhToan.get()
                            .getIdPhuongThucThanhToan()
                            .getTenPhuongThuc();
        }

        HoaDonDetailResponse response =
                new HoaDonDetailResponse();

        response.setId(hd.getId());
        response.setMaHoaDon(hd.getMaHoaDon());

        if (hd.getIdKhachHang() != null) {

            response.setIdKhachHang(
                    hd.getIdKhachHang().getId());

            response.setTenKhachHang(
                    hd.getIdKhachHang().getHoTen());

            response.setSoDienThoaiKhachHang(
                    hd.getIdKhachHang().getSoDienThoai());
        }

        response.setTenNguoiNhan(
                hd.getTenNguoiNhan());

        response.setSoDienThoaiNguoiNhan(
                hd.getSoDienThoaiNguoiNhan());

        response.setDiaChiGiaoHang(
                hd.getDiaChiGiaoHang());

        response.setLoaiHoaDon(
                hd.getLoaiHoaDon());

        response.setTrangThai(
                hd.getTrangThai());

        response.setGhiChu(
                hd.getGhiChu());

        response.setTongTienHang(
                hd.getTongTienHang());

        response.setTongGiamGia(
                hd.getTongGiamGia());

        response.setPhiVanChuyen(
                hd.getPhiVanChuyen());

        response.setTongThanhToan(
                hd.getTongThanhToan());

        response.setNgayTao(
                hd.getNgayTao());

        response.setNgayCapNhat(
                hd.getNgayCapNhat());

        response.setPhuongThucThanhToan(
                phuongThuc);

        response.setSanPhams(
                sanPhams);

        return response;
    }
}
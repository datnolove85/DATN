package com.example.backend.Service.impl;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Request.SanPhamTraRequest;
import com.example.backend.Request.TraHangRequest;
import com.example.backend.Response.HoaDonDetailResponse;
import com.example.backend.Response.SanPhamTraResponse;
import com.example.backend.Response.TraHangResponse;
import com.example.backend.Service.HoaDonService;
import com.example.backend.Service.TraHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Service

public class TraHangServiceImpl implements TraHangService {
    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private TraHangChiTietRepository traHangChiTietRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private TraHangRepository traHangRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Override
    public TraHangResponse getThongTinTraHang(Integer hoaDonId) {

        TraHangResponse response = new TraHangResponse();

        // 1. Lấy thông tin hóa đơn
        HoaDonDetailResponse hoaDon =
                hoaDonService.getDetail(hoaDonId);

        // 2. Lấy danh sách sản phẩm có thể trả
        List<SanPhamTraResponse> sanPhams =
                hoaDonService.getDanhSachTra(hoaDonId);

        // 3. Gộp lại
        response.setHoaDon(hoaDon);
        response.setSanPhams(sanPhams);

        return response;
    }

    @Override
    @Transactional
    public void traHang(TraHangRequest request) {

        HoaDon hoaDon = hoaDonRepository.findById(request.getHoaDonId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        TraHang traHang = new TraHang();
        traHang.setHoaDon(hoaDon);
        traHang.setLyDo(request.getLyDo());

        BigDecimal tongHoan = BigDecimal.ZERO;

        for (SanPhamTraRequest item : request.getDanhSachTra()) {

            HoaDonChiTiet hdct = hoaDonChiTietRepository.findById(item.getHdctId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy HDCT"));

            Integer daTra = traHangChiTietRepository.tongDaTra(hdct.getId());
            Integer conLai = hdct.getSoLuong() - daTra;

            if (item.getSoLuongTra() > conLai) {
                throw new RuntimeException("Vượt số lượng còn lại");
            }

            BigDecimal thanhTien =
                    hdct.getDonGia().multiply(BigDecimal.valueOf(item.getSoLuongTra()));

            TraHangChiTiet ct = new TraHangChiTiet();
            ct.setHoaDonChiTiet(hdct);
            ct.setSoLuongTra(item.getSoLuongTra());
            ct.setDonGiaTra(hdct.getDonGia());
            ct.setThanhTien(thanhTien);

            // ⭐ BẮT BUỘC: GẮN 2 CHIỀU
            ct.setTraHang(traHang);
            traHang.getChiTiets().add(ct);

            // update kho
            SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();
            spct.setSoLuongTon(spct.getSoLuongTon() + item.getSoLuongTra());
            sanPhamChiTietRepository.save(spct);

            tongHoan = tongHoan.add(thanhTien);
        }

        traHang.setTongTienHoan(tongHoan);

        // ⭐ CHỈ 1 SAVE DUY NHẤT
        traHangRepository.save(traHang);
    }
}
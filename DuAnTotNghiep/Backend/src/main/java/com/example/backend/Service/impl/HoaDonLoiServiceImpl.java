package com.example.backend.Service.impl;

import com.example.backend.Request.HuyDonLoiRequest;
import com.example.backend.Request.HuyHangLoatLoiRequest;
import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Entity.TrangThaiHoaDon;
import com.example.backend.Repository.HoaDonChiTietRepository;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Service.HoaDonLoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HoaDonLoiServiceImpl implements HoaDonLoiService {

    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    @Override
    public Page<HoaDon> timDonHangChuaGiaoChuaSanPhamLoi(String keyword, int page, int size) {
        return hoaDonRepository.findDonHangChuaGiaoBySanPhamKeyword(keyword, PageRequest.of(page, size));
    }

    @Override
    @Transactional
    public void huyDonLoiLe(Integer hoaDonId, HuyDonLoiRequest request) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn ID: " + hoaDonId));

        xuLyHuyDonVaHoanKho(hoaDon, request.getLyDoLoi());
        hoaDonRepository.save(hoaDon);
    }

    @Override
    @Transactional
    public void huyHangLoatLoi(HuyHangLoatLoiRequest request) {
        List<HoaDon> danhSachHoaDon = hoaDonRepository.findByIdIn(request.getDanhSachHoaDonId());

        for (HoaDon hoaDon : danhSachHoaDon) {
            xuLyHuyDonVaHoanKho(hoaDon, request.getLyDoLoi());
        }

        hoaDonRepository.saveAll(danhSachHoaDon);
    }

    // 🟢 Logic dùng chung: Đổi trạng thái, nối ghi chú lỗi, GIẢI PHÓNG TẠM GIỮ
    private void xuLyHuyDonVaHoanKho(HoaDon hoaDon, String lyDoLoi) {
        // Nếu đã hủy hoặc đã hoàn thành thì bỏ qua
        if (TrangThaiHoaDon.DA_HUY.getValue().equalsIgnoreCase(hoaDon.getTrangThai()) ||
                TrangThaiHoaDon.HOAN_THANH.getValue().equalsIgnoreCase(hoaDon.getTrangThai())) {
            return;
        }

        // 1. Cập nhật trạng thái và thời gian cập nhật
        hoaDon.setTrangThai(TrangThaiHoaDon.DA_HUY.getValue());
        hoaDon.setNgayCapNhat(LocalDateTime.now());

        // 2. Ghi lý do hủy vào trường ghiChu
        String ghiChuMoi = "[HỦY DO SP LỖI]: " + (lyDoLoi != null ? lyDoLoi : "Không có lý do cụ thể");
        if (hoaDon.getGhiChu() != null && !hoaDon.getGhiChu().isBlank()) {
            hoaDon.setGhiChu(hoaDon.getGhiChu() + " | " + ghiChuMoi);
        } else {
            hoaDon.setGhiChu(ghiChuMoi);
        }

        // 3. 🟢 GIẢI PHÓNG SỐ LƯỢNG TẠM GIỮ (soLuongTamGiu) TRONG SanPhamChiTiet
        List<HoaDonChiTiet> chiTiets = hoaDonChiTietRepository.findByIdHoaDon_Id(hoaDon.getId());
        for (HoaDonChiTiet ct : chiTiets) {
            SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();

            if (spct != null && ct.getSoLuong() != null) {
                int tamGiuHienTai = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;
                int tamGiuMoi = tamGiuHienTai - ct.getSoLuong();

                // Đảm bảo không bị âm
                if (tamGiuMoi < 0) {
                    tamGiuMoi = 0;
                }

                spct.setSoLuongTamGiu(tamGiuMoi);
                sanPhamChiTietRepository.save(spct);
            }
        }
    }
}
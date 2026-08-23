package com.example.backend.Service.impl;

import com.example.backend.Entity.KhachHang;
import com.example.backend.Entity.SanPham;
import com.example.backend.Entity.YeuThich;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.Repository.SanPhamRepository;
import com.example.backend.Repository.YeuThichRepository;
import com.example.backend.Service.YeuThichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class YeuThichServiceImpl implements YeuThichService {

    @Autowired
    private YeuThichRepository yeuThichRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Override
    public List<YeuThich> getDanhSachYeuThich(Integer idKhachHang) {
        return yeuThichRepository.findByKhachHangId(idKhachHang);
    }

    @Override
    public boolean toggleYeuThich(Integer idKhachHang, Integer idSanPham) {
        Optional<YeuThich> existing = yeuThichRepository.findByKhachHangIdAndSanPhamId(idKhachHang, idSanPham);

        if (existing.isPresent()) {
            // Nếu đã tồn tại -> Xóa (Bỏ thích)
            yeuThichRepository.delete(existing.get());
            return false;
        } else {
            // Nếu chưa có -> Thêm mới (Thả tim)
            KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
            SanPham sanPham = sanPhamRepository.findById(idSanPham)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

            YeuThich yeuThich = new YeuThich();
            yeuThich.setKhachHang(khachHang);
            yeuThich.setSanPham(sanPham);
            yeuThich.setNgayTao(LocalDateTime.now());

            yeuThichRepository.save(yeuThich);
            return true;
        }
    }

    @Override
    public boolean kiemTraDaThich(Integer idKhachHang, Integer idSanPham) {
        return yeuThichRepository.existsByKhachHangIdAndSanPhamId(idKhachHang, idSanPham);
    }
}
package com.example.backend.Service.impl;


import com.example.backend.Entity.KhachHang;
import com.example.backend.Entity.SanPham;
import com.example.backend.Entity.YeuThich;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.Repository.SanPhamRepository;
import com.example.backend.Repository.YeuThichRepository;
import com.example.backend.Response.YeuThichResponseDTO;
import com.example.backend.Service.YeuThichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<YeuThichResponseDTO> getDanhSachYeuThich(Integer idKhachHang) {
        List<Object[]> rows = yeuThichRepository.getDanhSachYeuThichRaw(idKhachHang);
        List<YeuThichResponseDTO> result = new ArrayList<>();

        for (Object[] row : rows) {
            Integer idSanPham = row[0] != null ? ((Number) row[0]).intValue() : null;
            String maSanPham = (String) row[1];
            String tenSanPham = (String) row[2];
            String tenThuongHieu = (String) row[3];
            String tenDanhMuc = (String) row[4];
            BigDecimal giaBan = row[5] != null ? (BigDecimal) row[5] : BigDecimal.ZERO;

            // Xử lý chuẩn hóa đường dẫn ảnh chuẩn như API shop của bạn
            String linkAnh = (String) row[6];
            if (linkAnh != null && !linkAnh.isEmpty()) {
                linkAnh = linkAnh.startsWith("/sanpham/") ? linkAnh : "/sanpham/" + linkAnh;
            } else {
                linkAnh = "/sanpham/default.webp";
            }

            LocalDateTime ngayTao = null;
            if (row[7] != null) {
                if (row[7] instanceof Timestamp) {
                    ngayTao = ((Timestamp) row[7]).toLocalDateTime();
                } else if (row[7] instanceof LocalDateTime) {
                    ngayTao = (LocalDateTime) row[7];
                }
            }

            result.add(new YeuThichResponseDTO(
                    idSanPham, maSanPham, tenSanPham, tenThuongHieu,
                    tenDanhMuc, giaBan, linkAnh, ngayTao
            ));
        }
        return result;
    }

    @Override
    public boolean toggleYeuThich(Integer idKhachHang, Integer idSanPham) {
        Optional<YeuThich> existing = yeuThichRepository.findByKhachHangIdAndSanPhamId(idKhachHang, idSanPham);

        if (existing.isPresent()) {
            yeuThichRepository.delete(existing.get());
            return false;
        } else {
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
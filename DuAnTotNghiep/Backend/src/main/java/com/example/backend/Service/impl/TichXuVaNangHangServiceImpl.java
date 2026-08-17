package com.example.backend.Service.impl;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Service.TichXuVaNangHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TichXuVaNangHangServiceImpl implements TichXuVaNangHangService {

    // Đọc mã cấu hình tích xu từ application.properties (Mặc định là "TI_LE_TICH_XU" nếu không tìm thấy)
    @Value("${app.config.ma-tich-xu:TI_LE_TICH_XU}")
    private String maTichXuConfig;

    private final HoaDonRepository hoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private final CauHinhHeThongRepository cauHinhHeThongRepository;
    private final CauHinhHangThanhVienRepository cauHinhHangThanhVienRepository;
    private final LichSuXuRepository lichSuXuRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void xuLyHoanTatDonHang(Integer idHoaDon) {
        // 1. Lấy thông tin hóa đơn
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn ID: " + idHoaDon));

        // Kiểm tra xem hóa đơn có thuộc về khách hàng thành viên không (khách vãng lai thì bỏ qua tích xu)
        if (hoaDon.getIdKhachHang() == null) {
            return;
        }

        KhachHang khachHang = hoaDon.getIdKhachHang();
        String moTaGiaoDich = "Tích xu từ đơn hàng: " + hoaDon.getMaHoaDon();

        // 2. LOGIC THỰC TẾ: Chống cộng trùng lặp (Idempotency Check)
        boolean daCongXu = lichSuXuRepository.existsByMoTaContaining(hoaDon.getMaHoaDon());
        if (daCongXu) {
            throw new RuntimeException("Hóa đơn " + hoaDon.getMaHoaDon() + " đã được cộng xu trước đó!");
        }

        // 3. Lấy tỷ lệ tích xu từ bảng cấu hình hệ thống bằng biến cấu hình linh động
        BigDecimal tyLeDinhMuc = cauHinhHeThongRepository.findByMaCauHinh(maTichXuConfig)
                .map(CauHinhHeThong::getGiaTriSo)
                .orElse(new BigDecimal("10000")); // Mặc định 10k nếu ko tìm thấy

        BigDecimal tongThanhToan = hoaDon.getTongThanhToan() != null ? hoaDon.getTongThanhToan() : BigDecimal.ZERO;

        // 4. Cập nhật thông số chi tiêu trước để tính chuẩn hạng mới nhất của khách
        BigDecimal chiTieuCu = khachHang.getTongChiTieu() != null ? khachHang.getTongChiTieu() : BigDecimal.ZERO;
        BigDecimal chiTieuMoi = chiTieuCu.add(tongThanhToan);
        khachHang.setTongChiTieu(chiTieuMoi);

        // 5. LOGIC MỚI: Xét và Nâng Hạng Thành Viên tự động, đồng thời lấy hệ số tích xu của hạng đó
        List<CauHinhHangThanhVien> danhSachHang = cauHinhHangThanhVienRepository.findByTrangThaiTrueOrderByNguongChiTieuToiThieuAsc();
        String hangMoi = khachHang.getHangThanhVien();
        BigDecimal heSoTichXuHang = BigDecimal.ONE; // Mặc định hệ số là 1.0 nếu chưa đạt hạng nào

        for (CauHinhHangThanhVien hang : danhSachHang) {
            if (chiTieuMoi.compareTo(hang.getNguongChiTieuToiThieu()) >= 0) {
                hangMoi = hang.getTenHang();
                if (hang.getTyLeTichXu() != null) {
                    heSoTichXuHang = BigDecimal.valueOf(hang.getTyLeTichXu()); // Cập nhật hệ số theo hạng đạt được cao nhất
                }
            }
        }
        khachHang.setHangThanhVien(hangMoi);

        // 6. Tính số xu nhận được (Có nhân với hệ số tích xu của hạng thành viên)
        // Công thức: Xu = (Tổng thanh toán / Định mức) * Hệ số hạng
        int soXuNhan = 0;
        if (tyLeDinhMuc.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal soXusCoBan = tongThanhToan.divide(tyLeDinhMuc, 4, RoundingMode.DOWN);
            BigDecimal soXusThucTe = soXusCoBan.multiply(heSoTichXuHang);
            soXuNhan = soXusThucTe.setScale(0, RoundingMode.DOWN).intValue(); // Làm tròn xuống
        }

        // 7. Cập nhật số dư xu và số lần mua cho Khách Hàng
        int soDuCu = khachHang.getSoDuXu() != null ? khachHang.getSoDuXu() : 0;
        int soDuSau = soDuCu + soXuNhan;
        khachHang.setSoDuXu(soDuSau);

        int soLanMuaCu = khachHang.getSoLanMua() != null ? khachHang.getSoLanMua() : 0;
        khachHang.setSoLanMua(soLanMuaCu + 1);

        // Lưu thông tin khách hàng xuống DB
        khachHangRepository.save(khachHang);

        // 8. Ghi lại lịch sử biến động xu (Bảng lich_su_xu)
        if (soXuNhan > 0) {
            LichSuXu lichSuXu = new LichSuXu();
            lichSuXu.setIdKhachHang(khachHang);
            lichSuXu.setSoXuThayDoi(soXuNhan);
            lichSuXu.setSoDuTruoc(soDuCu);
            lichSuXu.setSoDuSau(soDuSau);
            lichSuXu.setLoaiGiaoDich("TICH_LUY_DON_HANG");
            lichSuXu.setMoTa(moTaGiaoDich);

            lichSuXuRepository.save(lichSuXu);
        }
    }
}
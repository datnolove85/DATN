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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TichXuVaNangHangServiceImpl implements TichXuVaNangHangService {

    // Đọc cấu hình từ application.properties
    @Value("${app.config.ma-tich-xu:TI_LE_TICH_XU}")
    private String maTichXuConfig;

    @Value("${app.config.ma-chu-ky-xet-hang:CHU_KY_XET_HANG}")
    private String maChuKyConfig;

    private final HoaDonRepository hoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private final CauHinhHeThongRepository cauHinhHeThongRepository;
    private final CauHinhHangThanhVienRepository cauHinhHangThanhVienRepository;
    private final LichSuXuRepository lichSuXuRepository;

    // Múi giờ chuẩn Việt Nam để tránh lệch giờ
    private static final ZoneId ZONE_VN = ZoneId.of("Asia/Ho_Chi_Minh");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void xuLyHoanTatDonHang(Integer idHoaDon) {
        // 1. Lấy thông tin hóa đơn
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn ID: " + idHoaDon));

        if (hoaDon.getIdKhachHang() == null) {
            return; // Khách vãng lai bỏ qua
        }

        KhachHang khachHang = hoaDon.getIdKhachHang();
        String moTaGiaoDich = "Tích xu từ đơn hàng: " + hoaDon.getMaHoaDon();

        // 2. Chống cộng trùng lặp (Idempotency Check)
        boolean daCongXu = lichSuXuRepository.existsByMoTaContaining(hoaDon.getMaHoaDon());
        if (daCongXu) {
            throw new RuntimeException("Hóa đơn " + hoaDon.getMaHoaDon() + " đã được cộng xu trước đó!");
        }

        // 3. Lấy tỷ lệ tích xu từ cấu hình hệ thống
        BigDecimal tyLeDinhMuc = cauHinhHeThongRepository.findByMaCauHinh(maTichXuConfig)
                .map(CauHinhHeThong::getGiaTriSo)
                .orElse(new BigDecimal("10000"));

        BigDecimal tongThanhToan = hoaDon.getTongThanhToan() != null ? hoaDon.getTongThanhToan() : BigDecimal.ZERO;

        // === GỌI HÀM KIỂM TRA & XỬ LÝ CHU KỲ (ĐÃ FIX CHUẨN MÚI GIỜ & HẠ HẠNG ĐÚNG MỨC) ===
        kiemTraVaXuLyChuKy(khachHang);

        // 4. Cộng dồn chi tiêu vào chu kỳ hiện tại
        BigDecimal chiTieuCu = khachHang.getTongChiTieu() != null ? khachHang.getTongChiTieu() : BigDecimal.ZERO;
        BigDecimal chiTieuMoi = chiTieuCu.add(tongThanhToan);
        khachHang.setTongChiTieu(chiTieuMoi);

        // 5. Xét và Nâng/Giữ Hạng Thành Viên tự động dựa trên tổng chi tiêu mới
        List<CauHinhHangThanhVien> danhSachHang = cauHinhHangThanhVienRepository.findByTrangThaiTrueOrderByNguongChiTieuToiThieuAsc();
        String hangMoi = khachHang.getHangThanhVien();
        BigDecimal heSoTichXuHang = BigDecimal.ONE;

        for (CauHinhHangThanhVien hang : danhSachHang) {
            if (chiTieuMoi.compareTo(hang.getNguongChiTieuToiThieu()) >= 0) {
                hangMoi = hang.getTenHang();
                if (hang.getTyLeTichXu() != null) {
                    heSoTichXuHang = BigDecimal.valueOf(hang.getTyLeTichXu());
                }
            }
        }
        khachHang.setHangThanhVien(hangMoi);

        // 6. Tính số xu nhận được
        int soXuNhan = 0;
        if (tyLeDinhMuc.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal soXusCoBan = tongThanhToan.divide(tyLeDinhMuc, 4, RoundingMode.DOWN);
            BigDecimal soXusThucTe = soXusCoBan.multiply(heSoTichXuHang);
            soXuNhan = soXusThucTe.setScale(0, RoundingMode.DOWN).intValue();
        }

        // 7. Cập nhật số dư xu và số lần mua
        int soDuCu = khachHang.getSoDuXu() != null ? khachHang.getSoDuXu() : 0;
        int soDuSau = soDuCu + soXuNhan;
        khachHang.setSoDuXu(soDuSau);

        int soLanMuaCu = khachHang.getSoLanMua() != null ? khachHang.getSoLanMua() : 0;
        khachHang.setSoLanMua(soLanMuaCu + 1);

        khachHangRepository.save(khachHang);

        // 8. Ghi lại lịch sử biến động xu
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

    /**
     * Hàm kiểm tra chu kỳ xét hạng - Đảm bảo chuẩn múi giờ VN & hạ hạng đúng theo số tiền chi tiêu thực tế
     */
    private void kiemTraVaXuLyChuKy(KhachHang khachHang) {
        Instant now = Instant.now();

        // Đọc số tháng chu kỳ từ database (mặc định 12 tháng nếu không tìm thấy)
        int soThangChuKy = cauHinhHeThongRepository.findByMaCauHinh(maChuKyConfig)
                .map(c -> c.getGiaTriSo().intValue())
                .orElse(12);

        // Nếu khách mới chưa có mốc chu kỳ -> Khởi tạo mốc từ thời điểm hiện tại theo giờ VN
        if (khachHang.getNgayBatDauChuKy() == null || khachHang.getNgayHetHanHang() == null) {
            khachHang.setNgayBatDauChuKy(now);
            LocalDateTime expiredLdt = LocalDateTime.ofInstant(now, ZONE_VN).plusMonths(soThangChuKy);
            khachHang.setNgayHetHanHang(expiredLdt.atZone(ZONE_VN).toInstant());
            return;
        }

        // Nếu thời điểm hiện tại ĐÃ VƯỢT QUÁ ngày hết hạn chu kỳ (Hết năm 1 sang năm 2)
        if (now.isAfter(khachHang.getNgayHetHanHang())) {
            // Lấy tổng chi tiêu của chu kỳ vừa qua để xét hạng lại
            BigDecimal chiTieuChuKyCu = khachHang.getTongChiTieu() != null ? khachHang.getTongChiTieu() : BigDecimal.ZERO;

            List<CauHinhHangThanhVien> danhSachHang = cauHinhHangThanhVienRepository.findByTrangThaiTrueOrderByNguongChiTieuToiThieuAsc();

            // Tìm hạng tương ứng với số tiền đã chi tiêu ở chu kỳ cũ
            String hangMoiPhuHop = null;
            if (!danhSachHang.isEmpty()) {
                hangMoiPhuHop = danhSachHang.get(0).getTenHang(); // Mặc định rớt về hạng thấp nhất
            }

            for (CauHinhHangThanhVien hang : danhSachHang) {
                if (chiTieuChuKyCu.compareTo(hang.getNguongChiTieuToiThieu()) >= 0) {
                    hangMoiPhuHop = hang.getTenHang(); // Đạt mức nào thì giữ hạng đó
                }
            }

            // Cập nhật hạng mới (hạ xuống đúng mức chi tiêu thực tế của năm cũ)
            khachHang.getHangThanhVien(); // Có thể log lại nếu cần
            khachHang.setHangThanhVien(hangMoiPhuHop);

            // Reset tổng chi tiêu về 0 để bắt đầu tích lũy cho chu kỳ mới
            khachHang.setTongChiTieu(BigDecimal.ZERO);

            // Thiết lập mốc chu kỳ mới (cộng thêm số tháng cấu hình theo chuẩn giờ VN)
            khachHang.setNgayBatDauChuKy(now);
            LocalDateTime newExpiredLdt = LocalDateTime.ofInstant(now, ZONE_VN).plusMonths(soThangChuKy);
            khachHang.setNgayHetHanHang(newExpiredLdt.atZone(ZONE_VN).toInstant());
        }
    }
}
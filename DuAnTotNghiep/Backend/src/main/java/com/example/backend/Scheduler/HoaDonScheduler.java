package com.example.backend.Scheduler;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Entity.HoaDonVoucher;
import com.example.backend.Entity.LichSuHoaDon;
import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Repository.HoaDonChiTietRepository;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Repository.HoaDonVoucherRepository;
import com.example.backend.Repository.LichSuHoaDonRepository;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Service.payment.VoucherConsumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HoaDonScheduler {

    private final HoaDonRepository hoaDonRepo;
    private final HoaDonChiTietRepository hdctRepo;
    private final SanPhamChiTietRepository spctRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final HoaDonVoucherRepository hoaDonVoucherRepo;
    private final VoucherConsumeService voucherConsumeService;
    private final LichSuHoaDonRepository lichSuHoaDonRepository; // 🟢 Thêm repository lịch sử hóa đơn

    @Transactional
    @Scheduled(fixedRate = 5000) // 5 giây
    public void autoCancelHoaDon() {

        // POS: 15 phút
        LocalDateTime posExpiredTime = LocalDateTime.now().minusMinutes(15);

        // Online: 12 tiếng
        LocalDateTime onlineExpiredTime = LocalDateTime.now().minusHours(12);

        List<HoaDon> hoaDons = new java.util.ArrayList<>();

        hoaDons.addAll(
                hoaDonRepo.findExpiredHoaDonByLoai("tai_quay", posExpiredTime)
        );

        hoaDons.addAll(
                hoaDonRepo.findExpiredHoaDonByLoai("online", onlineExpiredTime)
        );

        for (HoaDon hd : hoaDons) {

            List<HoaDonChiTiet> ds = hdctRepo.findByIdHoaDon_Id(hd.getId());

            for (HoaDonChiTiet ct : ds) {

                SanPhamChiTiet sp = ct.getIdSanPhamChiTiet();
                if (sp == null) continue;

                int soLuongHuy = ct.getSoLuong();

                int tamGiuHienTai = sp.getSoLuongTamGiu() != null
                        ? sp.getSoLuongTamGiu()
                        : 0;

                sp.setSoLuongTamGiu(Math.max(0, tamGiuHienTai - soLuongHuy));

                spctRepo.save(sp);
            }

            String trangThaiCu = hd.getTrangThai();
            String trangThaiMoi = "da_huy";

            hd.setTrangThai(trangThaiMoi);

            String ghiChuTbl;
            if ("tai_quay".equalsIgnoreCase(hd.getLoaiHoaDon())) {
                ghiChuTbl = "Tự động hủy do quá 15 phút không thao tác.";
            } else {
                ghiChuTbl = "Tự động hủy do quá 12 giờ không hoàn tất đơn hàng.";
            }

            // Gộp hoặc gán ghi chú
            if (hd.getGhiChu() != null && !hd.getGhiChu().isBlank()) {
                hd.setGhiChu(hd.getGhiChu() + " | " + ghiChuTbl);
            } else {
                hd.setGhiChu(ghiChuTbl);
            }

            hd.setNgayCapNhat(LocalDateTime.now());
            hoaDonRepo.save(hd);

            // 🟢 Ghi nhận lịch sử hóa đơn với nguồn là "HỆ THỐNG"
            LichSuHoaDon lichSu = new LichSuHoaDon();
            lichSu.setHoaDon(hd);
            lichSu.setTrangThaiCu(trangThaiCu);
            lichSu.setTrangThaiMoi(trangThaiMoi);
            lichSu.setThoiGian(LocalDateTime.now());
            lichSu.setGhiChu(ghiChuTbl);
            lichSu.setNguonThaoTac("SYSTEM");
            lichSu.setNhanVien(null); // Do hệ thống tự động chạy nên không có nhân viên cụ thể

            lichSuHoaDonRepository.save(lichSu);

            HoaDonVoucher hdVoucher = hoaDonVoucherRepo
                    .findByIdHoaDon_Id(hd.getId())
                    .orElse(null);

            if (hdVoucher != null) {
                if (Boolean.TRUE.equals(hdVoucher.getDaConsume())) {
                    voucherConsumeService.returnVoucher(hd.getId());
                }

                hoaDonVoucherRepo.delete(hdVoucher);
            }

            Map<String, Object> event = new HashMap<>();
            event.put("type", "ORDER_CANCELLED");
            event.put("orderId", hd.getId());
            event.put("maHoaDon", hd.getMaHoaDon());
            event.put("message",
                    "Hóa đơn " + hd.getMaHoaDon() + " đã tự động hủy.");

            messagingTemplate.convertAndSend(
                    "/topic/pos",
                    (Object) event
            );
            System.out.println("Đã tự động hủy hóa đơn " + hd.getMaHoaDon());
        }
    }
}
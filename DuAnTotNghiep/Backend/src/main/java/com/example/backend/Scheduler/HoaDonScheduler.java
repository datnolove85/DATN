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
import com.example.backend.Service.HoaDonService;
import com.example.backend.Service.payment.VoucherConsumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class HoaDonScheduler {

    private final HoaDonRepository hoaDonRepo;
    private final HoaDonChiTietRepository hdctRepo;
    private final SanPhamChiTietRepository spctRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final HoaDonVoucherRepository hoaDonVoucherRepo;
    private final VoucherConsumeService voucherConsumeService;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final HoaDonService hoaDonService;

    @Transactional
    @Scheduled(fixedRate = 5000) // 5 giây chạy 1 lần
    public void autoCancelHoaDon() {

        // 1. Mốc thời gian
        LocalDateTime posExpiredTime = LocalDateTime.now().minusMinutes(15);         // POS: 15 phút
        LocalDateTime onlineNoPaymentTime = LocalDateTime.now().minusMinutes(10);    // Online chưa chọn PTTT: 10 phút
        LocalDateTime onlineExpiredTime = LocalDateTime.now().minusHours(12);        // Online đã chọn PTTT: 12 tiếng

        // 2. Lấy dữ liệu từ các hàm repository có sẵn
        List<HoaDon> taiQuayList = hoaDonRepo.findExpiredHoaDonByLoai("tai_quay", posExpiredTime);
        List<HoaDon> onlineNoPaymentList = hoaDonRepo.findOnlineWithoutPayment(onlineNoPaymentTime);
        List<HoaDon> onlineWithPaymentList = hoaDonRepo.findOnlineWithPayment(onlineExpiredTime);

        // 3. Gom vào Set để chống trùng lặp đơn hàng nếu thỏa nhiều điều kiện
        Set<HoaDon> hoaDons = new LinkedHashSet<>();
        hoaDons.addAll(taiQuayList);
        hoaDons.addAll(onlineNoPaymentList);
        hoaDons.addAll(onlineWithPaymentList);

        for (HoaDon hd : hoaDons) {

            // Hoàn lại số lượng tồn kho tạm giữ
            List<HoaDonChiTiet> ds = hdctRepo.findByIdHoaDon_Id(hd.getId());

            for (HoaDonChiTiet ct : ds) {
                SanPhamChiTiet sp = ct.getIdSanPhamChiTiet();
                if (sp == null) continue;

                int soLuongHuy = ct.getSoLuong();
                int tamGiuHienTai = sp.getSoLuongTamGiu() != null ? sp.getSoLuongTamGiu() : 0;

                sp.setSoLuongTamGiu(Math.max(0, tamGiuHienTai - soLuongHuy));
                spctRepo.save(sp);
            }

            String trangThaiCu = hd.getTrangThai();
            String trangThaiMoi = "da_huy";
            hd.setTrangThai(trangThaiMoi);

            // Ghi chú chi tiết cho từng loại hủy
            String ghiChuTbl;
            if (taiQuayList.contains(hd)) {
                ghiChuTbl = "Tự động hủy do quá 15 phút không thao tác.";
            } else if (onlineNoPaymentList.contains(hd)) {
                ghiChuTbl = "Tự động hủy do quá 10 phút không chọn phương thức thanh toán.";
            } else {
                ghiChuTbl = "Tự động hủy do quá 12 giờ không hoàn tất đơn hàng.";
            }

            if (hd.getGhiChu() != null && !hd.getGhiChu().isBlank()) {
                hd.setGhiChu(hd.getGhiChu() + " | " + ghiChuTbl);
            } else {
                hd.setGhiChu(ghiChuTbl);
            }

            hd.setNgayCapNhat(LocalDateTime.now());
            hoaDonRepo.save(hd);

            hoaDonService.hoanXuKhiHuyDon(hd.getId());


            // Ghi nhận lịch sử hóa đơn
            LichSuHoaDon lichSu = new LichSuHoaDon();
            lichSu.setHoaDon(hd);
            lichSu.setTrangThaiCu(trangThaiCu);
            lichSu.setTrangThaiMoi(trangThaiMoi);
            lichSu.setThoiGian(LocalDateTime.now());
            lichSu.setGhiChu(ghiChuTbl);
            lichSu.setNguonThaoTac("SYSTEM");
            lichSu.setNhanVien(null);

            lichSuHoaDonRepository.save(lichSu);

            // Hoàn voucher nếu có
            HoaDonVoucher hdVoucher = hoaDonVoucherRepo.findByIdHoaDon_Id(hd.getId()).orElse(null);
            if (hdVoucher != null) {
                if (Boolean.TRUE.equals(hdVoucher.getDaConsume())) {
                    voucherConsumeService.returnVoucher(hd.getId());
                }
                hdVoucher.setDaConsume(false); // Nếu trong Entity daConsume là kiểu Integer, hãy đổi thành: hdVoucher.setDaConsume(0);
                hoaDonVoucherRepo.save(hdVoucher);
            }

            // Gửi thông báo WebSocket
            Map<String, Object> event = new HashMap<>();
            event.put("type", "ORDER_CANCELLED");
            event.put("orderId", hd.getId());
            event.put("maHoaDon", hd.getMaHoaDon());
            event.put("message", "Hóa đơn " + hd.getMaHoaDon() + " đã tự động hủy.");

            messagingTemplate.convertAndSend("/topic/pos", (Object) event);
            System.out.println("Đã tự động hủy hóa đơn " + hd.getMaHoaDon());
        }
    }
}
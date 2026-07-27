package com.example.backend.Scheduler;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Repository.HoaDonChiTietRepository;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Repository.SanPhamChiTietRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
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

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void autoCancelHoaDon() {

        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);

        List<HoaDon> hoaDons = hoaDonRepo.findExpiredHoaDon(expiredTime);

        for (HoaDon hd : hoaDons) {

            List<HoaDonChiTiet> ds = hdctRepo.findByIdHoaDon_Id(hd.getId());

            for (HoaDonChiTiet ct : ds) {

                SanPhamChiTiet sp = ct.getIdSanPhamChiTiet();
                if (sp == null) continue;

                int soLuongHuy = ct.getSoLuong();

                // 1. Giảm số lượng tạm giữ (không để âm)
                int tamGiuHienTai = sp.getSoLuongTamGiu() != null ? sp.getSoLuongTamGiu() : 0;
                int tamGiuMoi = Math.max(0, tamGiuHienTai - soLuongHuy);
                sp.setSoLuongTamGiu(tamGiuMoi);

                // 2. Số lượng tồn THỰC TẾ (soLuongTon): GIỮ NGUYÊN, không động vào
                int tonHienTai = sp.getSoLuongTon() != null ? sp.getSoLuongTon() : 0;



                spctRepo.save(sp);
            }

            hd.setTrangThai("da_huy");
            hd.setGhiChu("Tự động hủy do quá 15 phút không thao tác.");
            hd.setNgayCapNhat(LocalDateTime.now());

            hoaDonRepo.save(hd);
            // Tạo payload khớp với cấu trúc event của FE
            Map<String, Object> event = new HashMap<>();
            event.put("type", "ORDER_CANCELLED");
            event.put("orderId", hd.getId());
            event.put("maHoaDon", hd.getMaHoaDon());
            event.put("message", "Hóa đơn " + hd.getMaHoaDon() + " đã tự động hủy do quá 15 phút không thao tác.");

            messagingTemplate.convertAndSend(
                    "/topic/pos",
                    (Object) event
            );
            System.out.println("Đã tự động hủy hóa đơn " + hd.getMaHoaDon() + " và giải phóng số lượng tạm giữ.");
        }
    }

}
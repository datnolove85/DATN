package com.example.backend.Scheduler;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Repository.HoaDonChiTietRepository;
import com.example.backend.Repository.HoaDonRepository;
import com.example.backend.Repository.SanPhamChiTietRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HoaDonScheduler {

    private final HoaDonRepository hoaDonRepo;

    private final HoaDonChiTietRepository hdctRepo;

    private final SanPhamChiTietRepository spctRepo;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void autoCancelHoaDon() {

        LocalDateTime expiredTime =
                LocalDateTime.now().minusMinutes(15);

        List<HoaDon> hoaDons =
                hoaDonRepo.findExpiredHoaDon(expiredTime);

        for (HoaDon hd : hoaDons) {

            List<HoaDonChiTiet> ds =
                    hdctRepo.findByIdHoaDon_Id(hd.getId());

            // trả kho
            for (HoaDonChiTiet ct : ds) {

                SanPhamChiTiet sp =
                        ct.getIdSanPhamChiTiet();

                sp.setSoLuongTon(
                        sp.getSoLuongTon()
                                + ct.getSoLuong()
                );

                spctRepo.save(sp);
            }

            hd.setTrangThai("da_huy");

            hd.setGhiChu(
                    "Tự động hủy do quá 30 phút không thao tác."
            );

            hd.setNgayCapNhat(
                    LocalDateTime.now()
            );

            hoaDonRepo.save(hd);

            System.out.println(
                    "Đã tự động hủy hóa đơn "
                            + hd.getMaHoaDon()
            );
        }

    }

}
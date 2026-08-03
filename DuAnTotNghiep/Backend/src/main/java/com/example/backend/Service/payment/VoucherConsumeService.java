package com.example.backend.Service.payment;

import com.example.backend.Entity.HoaDonVoucher;
import com.example.backend.Entity.KhoVoucher;
import com.example.backend.Entity.Voucher;
import com.example.backend.Entity.VoucherCuaKhachHang;
import com.example.backend.Repository.HoaDonVoucherRepository;
import com.example.backend.Repository.KhoVoucherRepository;
import com.example.backend.Repository.VoucherCuaKhachHangRepository;
import com.example.backend.Repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoucherConsumeService {

    private final HoaDonVoucherRepository hoaDonVoucherRepo;
    private final VoucherRepository voucherRepo;
    private final KhoVoucherRepository khoVoucherRepository;
    private final VoucherCuaKhachHangRepository voucherCuaKhachHangRepo;

    @Transactional
    public void consumeVoucher(Integer idHoaDon) {

        HoaDonVoucher hdVoucher = hoaDonVoucherRepo
                .findByIdHoaDon_Id(idHoaDon)
                .orElse(null);

        if (hdVoucher == null) {
            return;
        }

        // Đã consume rồi thì bỏ qua
        if (Boolean.TRUE.equals(hdVoucher.getDaConsume())) {
            return;
        }

        // Voucher hệ thống
        if (hdVoucher.getIdVoucher() != null) {

            Voucher voucher = hdVoucher.getIdVoucher();

            voucher.setSoLuongDaDung(
                    voucher.getSoLuongDaDung() + 1
            );
            voucher.setSoLuong(voucher.getSoLuong()-1);

            voucherRepo.save(voucher);
        }

        // Voucher khách
        else if (hdVoucher.getVoucherCuaKhachHang() != null) {

            VoucherCuaKhachHang voucherKhach =
                    hdVoucher.getVoucherCuaKhachHang();

            voucherKhach.setTrangThai("DA_DUNG");

            voucherCuaKhachHangRepo.save(voucherKhach);

        }

        // Đánh dấu đã tiêu thụ
        hdVoucher.setDaConsume(true);
        hoaDonVoucherRepo.save(hdVoucher);
    }
    @Transactional
    public void returnVoucher(Integer idHoaDon) {

        HoaDonVoucher hdVoucher = hoaDonVoucherRepo
                .findByIdHoaDon_Id(idHoaDon)
                .orElse(null);

        if (hdVoucher == null || !Boolean.TRUE.equals(hdVoucher.getDaConsume())) {
            return;
        }

        // Voucher hệ thống
        if (hdVoucher.getIdVoucher() != null) {

            Voucher voucher = hdVoucher.getIdVoucher();

            voucher.setSoLuongDaDung(
                    Math.max(0, voucher.getSoLuongDaDung() - 1)
            );

            voucherRepo.save(voucher);
        }

        // Voucher khách
        else if (hdVoucher.getVoucherCuaKhachHang() != null) {

            VoucherCuaKhachHang voucherKhach = hdVoucher.getVoucherCuaKhachHang();
            voucherKhach.setTrangThai("CHUA_DUNG");
            voucherCuaKhachHangRepo.save(voucherKhach);

            KhoVoucher khoVoucher = hdVoucher.getIdKhoVoucher();
            khoVoucher.setSoLuongConLai(khoVoucher.getSoLuongConLai() + 1);
            khoVoucherRepository.save(khoVoucher);
        }

        hdVoucher.setDaConsume(false);
        hoaDonVoucherRepo.save(hdVoucher);
    }
}
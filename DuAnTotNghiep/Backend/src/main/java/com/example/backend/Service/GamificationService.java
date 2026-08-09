package com.example.backend.Service;

import com.example.backend.Response.LichSuXuResponse;
import com.example.backend.Response.TrangThaiGamificationResponse;
import com.example.backend.Entity.DiemDanhHangNgay;
import com.example.backend.Entity.LichSuXu;
import com.example.backend.Entity.PhanThuongMinigame;
import com.example.backend.Entity.KhoVoucher;
import com.example.backend.Entity.VoucherCuaKhachHang;
import com.example.backend.Response.VoucherCuaKhachHangResponse;

import java.util.List;

public interface GamificationService {
    // Phía Khách hàng
    DiemDanhHangNgay diemDanhHangNgay(Integer idKhachHang);
    PhanThuongMinigame quayThuong(Integer idKhachHang, String loaiGame);
    TrangThaiGamificationResponse getTrangThaiGamification(Integer idKhachHang);
    List<LichSuXuResponse> getLichSuXu(Integer idKhachHang);

    // === QUẢN LÝ VOUCHER & ĐỔI XU ===
    List<KhoVoucher> getAllKhoVoucher();
    VoucherCuaKhachHang doiXuLayVoucher(Integer idKhachHang, Integer idKhoVoucher);
    List<VoucherCuaKhachHangResponse> getVoucherCuaKhachHang(Integer idKhachHang);

    // === PHÍA ADMIN ===
    List<PhanThuongMinigame> getAllPhanThuong();
    PhanThuongMinigame createPhanThuong(PhanThuongMinigame phanThuong);
    PhanThuongMinigame updatePhanThuong(Integer id, PhanThuongMinigame phanThuongMoi);
    void deletePhanThuong(Integer id);
    List<LichSuXu> getAllLichSuXuSystem();

    // === PHÍA ADMIN: QUẢN LÝ KHO VOUCHER ===
    KhoVoucher createKhoVoucher(KhoVoucher khoVoucher);
    KhoVoucher updateKhoVoucher(Integer id, KhoVoucher khoVoucherMoi);
    void deleteKhoVoucher(Integer id);

    void updateSoLuotLatTheChoKhachHang(Integer idKhachHang, Integer soLuotMoi);

    void updateSoLuotLatTheChoToanBo(Integer soLuotMoi);

}
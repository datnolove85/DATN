package com.example.backend.Service;


import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.KhoVoucher;
import com.example.backend.Entity.TrangThaiHoaDon;
import com.example.backend.Entity.VoucherCuaKhachHang;
import com.example.backend.Request.*;
import com.example.backend.Response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface HoaDonService {

    List<HoaDonResponse> filter(String trangThai, String loaiHoaDon);

    List<HoaDonResponse> search(String keyword);

    Page<HoaDonResponse> getAll(Pageable pageable);

    List<HoaDonResponse> getAll();

    HoaDonDetailResponse getDetail(Integer id);

    HoaDon taoHoaDonCho(TaoHoaDonRequest taoHoaDonRequest);

    HoaDon taoHoaDonChoOnline();

    Map<String, Object> createOnlineOrder(CreateOnlineOrderRequest request, HttpServletRequest httpServletRequest);

    List<HoaDon> getHoaDonCho(Integer idNhanVien);

    List<GioHangResponse> getChiTietHoaDon(
            Integer idHoaDon
    );

    void themSanPhamVaoHoaDon(
            ThemSanPhamRequest request
    );

    void xoaSanPhamKhoiHoaDon(Integer idHoaDonChiTiet);

    void tangSoLuong(Integer idHoaDonChiTiet);

    void giamSoLuong(Integer idHoaDonChiTiet);

    List<VoucherKhachHangResponse> ganKhachHang(Integer idHoaDon, Integer idKhachHang);

    @Transactional
    public Object thanhToanHoaDon(
            ThanhToanHoaDonRequest req, Integer idNhanVien
    );

    @Transactional
    public Object thanhToanHoaDonOnline(
            ThanhToanHoaDonRequest req
    );

    void huyHoaDon(Integer id, Integer idNhanVien);

    void huyHoaDonOnline(Integer id);

    void apVoucher(Integer idHoaDon, Integer idVoucher, Integer idVoucherKhachHang);

    void boVoucher(
            Integer idHoaDon
    );

    void recalculateHoaDon(
            Integer idHoaDon
    );

    Page<HoaDon> search(HoaDonFilterRequest req, Pageable pageable);

    List<SanPhamTraResponse> getDanhSachTra(Integer hoaDonId);

    Page<HoaDonResponse> searchOnline(
            HoaDonFilterRequest req,
            Pageable pageable
    );

    void updateTrangThai(
            Integer id,
            String trangThaiMoi,
            Integer idNhanVien
    );

    Map<String, Object> taoQr(Integer hoaDonId, Long amount);

    void capNhatHoaDonTheoVoucher(Integer idVoucher);

    void capNhatHoaDonTheoKhoVoucher(Integer idKhoVoucher);

    void capNhatSoLuong(Integer idHoaDonChiTiet, Integer soLuongMoi);

    List<KhoVoucher> goKhachHang(Integer idHoaDon);

    void hoanXuKhiHuyDon(Integer idHoaDon);

    void apDungXu(Integer idHoaDon, Integer soXuSuDung);

    void thayDoiTrangThai(Integer idHoaDon, String trangThaiMoi, String ghiChu, String nguonThaoTac, Integer idNhanVien);
}
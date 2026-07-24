package com.example.backend.Service;

import com.example.backend.Response.DonHangResponse;

import java.util.List;

public interface DonHangService {

    /**
     * Lấy toàn bộ đơn hàng của khách hàng đang đăng nhập
     *
     * @param idTaiKhoan lấy từ JWT
     */
    List<DonHangResponse> layDanhSachDonHang(Integer idTaiKhoan);

    /**
     * Lấy chi tiết một đơn hàng cho Khách hàng (có kiểm tra quyền sở hữu)
     *
     * @param idTaiKhoan lấy từ JWT
     * @param idHoaDon mã hóa đơn cần xem
     */
    DonHangResponse layChiTietDonHang(
            Integer idTaiKhoan,
            Integer idHoaDon
    );

    /**
     * Lấy chi tiết đơn hàng dành cho Admin/Nhân viên (không kiểm tra idTaiKhoan)
     *
     * @param idHoaDon mã hóa đơn cần xem
     */
    DonHangResponse layChiTietDonHangChoAdmin(Integer idHoaDon);

}
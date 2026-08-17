package com.example.backend.Service;

public interface TichXuVaNangHangService {
    /**
     * Xử lý tích xu, cộng tổng chi tiêu và nâng hạng cho khách hàng khi đơn hàng hoàn thành
     * @param idHoaDon ID của hóa đơn vừa chuyển sang trạng thái thành công
     */
    void xuLyHoanTatDonHang(Integer idHoaDon);


}
package com.example.backend.Service;

import com.example.backend.Entity.YeuThich;

import java.util.List;

public interface YeuThichService {
    List<YeuThich> getDanhSachYeuThich(Integer idKhachHang);

    // Thả tim / Bỏ tim (Toggle): Trả về true nếu đã thích, false nếu đã hủy thích
    boolean toggleYeuThich(Integer idKhachHang, Integer idSanPham);

    boolean kiemTraDaThich(Integer idKhachHang, Integer idSanPham);
}
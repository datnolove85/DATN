package com.example.backend.Repository;

import com.example.backend.Entity.LichSuXu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuXuRepository extends JpaRepository<LichSuXu, Integer> {

    // Lấy danh sách lịch sử xu của một khách hàng, sắp xếp mới nhất lên đầu
    List<LichSuXu> findByIdKhachHang_IdOrderByNgayTaoDesc(Integer idKhachHang);
}
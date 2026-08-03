package com.example.backend.Repository;

import com.example.backend.Entity.LichSuMinigame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LichSuMinigameRepository extends JpaRepository<LichSuMinigame, Integer> {

    // Lấy lịch sử minigame của khách hàng
    List<LichSuMinigame> findByIdKhachHang_IdOrderByNgayTaoDesc(Integer idKhachHang);

    // Đếm số lượt chơi của khách theo loại game trong khoảng thời gian (dùng để chặn giới hạn 3 lần/ngày)
    long countByIdKhachHang_IdAndLoaiGameAndNgayTaoBetween(
            Integer idKhachHang,
            String loaiGame,
            LocalDateTime tuNgay,
            LocalDateTime denNgay
    );
}
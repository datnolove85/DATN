package com.example.backend.Repository;

import com.example.backend.Entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {
    // Tìm lịch sử theo hóa đơn, sắp xếp thời gian tăng dần để vẽ Timeline
    @Query("SELECT l FROM LichSuHoaDon l WHERE l.hoaDon.id = :hoaDonId ORDER BY l.thoiGian ASC")
    List<LichSuHoaDon> findByHoaDonIdCustom(@Param("hoaDonId") Integer hoaDonId);


}
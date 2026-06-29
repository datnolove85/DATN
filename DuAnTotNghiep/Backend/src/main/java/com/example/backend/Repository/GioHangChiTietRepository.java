package com.example.backend.Repository;

import com.example.backend.Entity.GioHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet,Integer> {

    Optional<GioHangChiTiet> findByGioHang_IdAndSanPhamChiTiet_Id(
            Integer gioHangId,
            Integer sanPhamChiTietId
    );

    List<GioHangChiTiet> findByGioHang_Id(Integer gioHangId);
}
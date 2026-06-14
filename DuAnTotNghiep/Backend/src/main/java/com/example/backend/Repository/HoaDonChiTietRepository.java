package com.example.backend.Repository;

import com.example.backend.Entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    List<HoaDonChiTiet> findByIdHoaDon_Id(Integer idHoaDon);
    Optional<HoaDonChiTiet> findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(
            Integer idHoaDon,
            Integer idSanPhamChiTiet
    );
}

package com.example.backend.Repository;

import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Request.HoaDonChiTietWithAnhDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    List<HoaDonChiTiet> findByIdHoaDon_Id(Integer idHoaDon);
    Optional<HoaDonChiTiet> findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(
            Integer idHoaDon,
            Integer idSanPhamChiTiet
    );

    @Query("""
SELECT ct
FROM HoaDonChiTiet ct
WHERE ct.idHoaDon.id = :idHoaDon
""")
    List<HoaDonChiTiet> findByHoaDon(@Param("idHoaDon") Integer idHoaDon);
}

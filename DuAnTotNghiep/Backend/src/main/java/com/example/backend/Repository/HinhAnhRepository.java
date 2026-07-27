package com.example.backend.Repository;

import com.example.backend.Entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    List<HinhAnh> findByIdSanPhamChiTiet_IdAndTrangThaiTrue(Integer id);

    List<HinhAnh> findAllByIdIn(List<Integer> ids);

    Optional<HinhAnh> findFirstByIdSanPhamChiTiet_IdAndLaAnhChinhTrue(Integer idSanPhamChiTiet);

    Optional<HinhAnh> findFirstByIdSanPhamChiTiet_IdAndLaAnhChinhTrueAndTrangThaiTrue(
            Integer idSanPhamChiTiet
    );

    Optional<HinhAnh> findFirstByIdSanPhamChiTiet_IdAndTrangThaiTrueOrderByIdAsc(
            Integer idSanPhamChiTiet
    );

    @Modifying
    @Query("""
update HinhAnh h
set h.laAnhChinh = false
where h.idSanPhamChiTiet.id = :idSPCT
""")
    void boAnhChinh(@Param("idSPCT") Integer idSPCT);

    @Query("""
SELECT h
FROM HinhAnh h
JOIN h.idSanPhamChiTiet spct
JOIN spct.idSanPham sp
WHERE sp.id IN :ids
AND h.laAnhChinh=true
AND h.trangThai=true
""")
    List<HinhAnh> getMainImages(List<Integer> ids);
}

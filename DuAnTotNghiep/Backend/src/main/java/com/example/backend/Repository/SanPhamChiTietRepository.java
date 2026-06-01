package com.example.backend.Repository;

import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Response.SanPhamChiTietResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Integer> {
    @Query("""
select spct
from SanPhamChiTiet spct
where spct.trangThai = true
""")
    List<SanPhamChiTiet> getAllClient();

    @Query("""
select spct
from SanPhamChiTiet spct
where spct.idSanPham.id = :idSanPham
and spct.trangThai = true
""")
    List<SanPhamChiTiet> findVariantsByProduct(@Param("idSanPham") Integer idSanPham);

    @Query("""
select spct
from SanPhamChiTiet spct
where spct.idSanPham.id = :idSanPham
""")
    List<SanPhamChiTiet> findBySanPham(Integer idSanPham);

    @Query("""
select ha.idSanPhamChiTiet.id, ha.link
from HinhAnh ha
where ha.laAnhChinh = true
""")
    List<Object[]> getAllImages();

    @Query("""
select spct
from SanPhamChiTiet spct
where spct.id = :id
""")
    SanPhamChiTiet getDetailEntity(@Param("id") Integer id);

    @Query("""
select ha.link
from HinhAnh ha
where ha.idSanPhamChiTiet.id = :id
order by ha.id asc
""")
    List<String> getAllImages(@Param("id") Integer id);

    SanPhamChiTiet findFirstByIdSanPham_Id(Integer idSanPham);

}
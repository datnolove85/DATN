package com.example.backend.Repository;

import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Response.SanPhamChiTietResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet,Integer> {

    // LIST PRODUCT - 1 SPCT đại diện
    @Query("""
    select spct
    from SanPhamChiTiet spct
    where spct.trangThai = true
    and spct.id in (
        select min(s.id)
        from SanPhamChiTiet s
        group by s.idSanPham.id
    )
    """)
    List<SanPhamChiTiet> findRepresentativeSpct();

    // THUMBNAIL
    @Query("""
    select ha.idSanPhamChiTiet.id, ha.link
    from HinhAnh ha
    where ha.laAnhChinh = true
    """)
    List<Object[]> getThumbnailImages();

    // DETAIL VARIANTS
    @Query("""
    select spct
    from SanPhamChiTiet spct
    where spct.idSanPham.id = :idSanPham
    and spct.trangThai = true
    """)
    List<SanPhamChiTiet> findVariantsByProduct(@Param("idSanPham") Integer idSanPham);

    // DETAIL IMAGES
    @Query("""
    select ha.link
    from HinhAnh ha
    where ha.idSanPhamChiTiet.idSanPham.id = :idSanPham
    order by ha.id asc
    """)
    List<String> getImagesByProduct(@Param("idSanPham") Integer idSanPham);

    @Query("""
select ha.idSanPhamChiTiet.id, ha.link
from HinhAnh ha
""")
    List<Object[]> getAllImages();

    @Query("""
select ha.link
from HinhAnh ha
""")
    List<String> getAllImagesFlat();

}
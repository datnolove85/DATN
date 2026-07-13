package com.example.backend.Repository;

import com.example.backend.Entity.*;
import com.example.backend.Response.SanPhamChiTietResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {

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
            where ha.trangThai = true
            and ha.laAnhChinh = true
            and ha.idSanPhamChiTiet.trangThai = true
            """)
    List<Object[]> getThumbnailImages();


    // DETAIL VARIANTS
    @Query("""
            select spct
            from SanPhamChiTiet spct
            where spct.idSanPham.id = :idSanPham
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
            where ha.trangThai = true
            and ha.laAnhChinh = true
            order by ha.id
            """)
    List<Object[]> getAllImages();

    @Query("""
            select ha.idSanPhamChiTiet.id, ha.link
            from HinhAnh ha
            where ha.trangThai = true
            order by ha.idSanPhamChiTiet.id, ha.id
            """)
    List<Object[]> getAllImagesForVariant();


    @Query("""
            select ha.idSanPhamChiTiet.id, ha.link
            from HinhAnh ha
            where ha.trangThai = true
            and ha.idSanPhamChiTiet.idSanPham.id = :idSanPham
            order by ha.idSanPhamChiTiet.id, ha.id
            """)
    List<Object[]> getAllImageForShop(
            @Param("idSanPham") Integer idSanPham);

    @Query("""
            select ha.link
            from HinhAnh ha 
            """)
    List<String> getAllImagesFlat();

    boolean existsByIdSanPhamAndIdMauSacAndIdKichThuoc(
            SanPham sp,
            MauSac mau,
            KichThuoc size
    );

    List<SanPhamChiTiet> getSanPhamChiTietsByIdSanPham(@Param("idSanPham") Integer id);

    @Query("""
                select spct.idSanPham.id, coalesce(sum(spct.soLuongTon),0)
                from SanPhamChiTiet spct
                group by spct.idSanPham.id
            """)
    List<Object[]> tongSoLuongTheoSanPham();

    Optional<SanPhamChiTiet> findByIdSanPham_IdAndIdMauSac_IdAndIdKichThuoc_Id(
            Integer idSanPham,
            Integer idMauSac,
            Integer idKichThuoc);

    @Query("SELECT s FROM SanPhamChiTiet s WHERE s.trangThai = true")
    List<SanPhamChiTiet> findAllDangKinhDoanh();

    List<SanPhamChiTiet> findByIdSanPham_Id(Integer idSanPham);

    List<SanPhamChiTiet> findByIdSanPham(Integer idSanPham);

    @Query("""
            SELECT COUNT(spct)
            FROM SanPhamChiTiet spct
            WHERE spct.idSanPham.id = :idSanPham
            AND spct.trangThai = true
            """)
    Integer countDangKinhDoanh(@Param("idSanPham") Integer idSanPham);

    @Query("""
            SELECT COUNT(spct)
            FROM SanPhamChiTiet spct
            WHERE spct.idSanPham.id = :idSanPham
            """)
    Integer countTong(@Param("idSanPham") Integer idSanPham);

    @Query("""
            select spct
            from SanPhamChiTiet spct
            where spct.id in (
                select min(s.id)
                from SanPhamChiTiet s
                where s.trangThai = true
                group by s.idSanPham.id
            )
            """)
    List<SanPhamChiTiet> findRepresentativeSpctDangKinhDoanh();

    @Query("""
                select ha.link
                from HinhAnh ha
                where ha.idSanPhamChiTiet.idSanPham.id = :idSanPham
                and ha.laAnhChinh = true
                order by ha.id desc
            """)
    List<String> findAnhChinhBySanPhamId(
            @Param("idSanPham") Integer idSanPham
    );

    @Query("""
            select
                ha.idSanPhamChiTiet.id,
                ha.idSanPhamChiTiet.idMauSac.id,
                ha.idSanPhamChiTiet.idKichThuoc.id,
                ha.link
            from HinhAnh ha
            where ha.idSanPhamChiTiet.idSanPham.id = :idSanPham
            and ha.trangThai = true
            order by ha.id
            """)
    List<Object[]> getGalleryByProduct(@Param("idSanPham") Integer idSanPham);

    @Query("""
            SELECT ha.tenAnh
            FROM HinhAnh ha
            WHERE ha.idSanPhamChiTiet.id = :idSpct
            AND ha.laAnhChinh = true
            """)
    List<String> findAnhChinhBySanPhamChiTietId(
            @Param("idSpct") Integer idSpct
    );

    @Query("""
            SELECT ha.link
            FROM HinhAnh ha
            WHERE ha.idSanPhamChiTiet.id = :idSpct
              AND ha.laAnhChinh = true
              AND ha.trangThai = true
            """)
    List<String> findLinkAnhChinhBySanPhamChiTietId(
            @Param("idSpct") Integer idSpct
    );

    // Chỉ lấy những sản phẩm chưa nằm trong đợt giảm giá nào đang diễn ra/sắp diễn ra
    @Query("""
            SELECT spct
            FROM SanPhamChiTiet spct
            WHERE NOT EXISTS (
                SELECT 1
                FROM SanPhamGiamGia spgg
                WHERE spgg.sanPhamChiTiet.id = spct.id
            )
            """)
    List<SanPhamChiTiet> findSanPhamChuaApDung();

}
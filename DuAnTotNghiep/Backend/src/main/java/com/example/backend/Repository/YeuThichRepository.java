package com.example.backend.Repository;

import com.example.backend.Entity.YeuThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YeuThichRepository extends JpaRepository<YeuThich, Integer> {

    Optional<YeuThich> findByKhachHangIdAndSanPhamId(Integer idKhachHang, Integer idSanPham);

    boolean existsByKhachHangIdAndSanPhamId(Integer idKhachHang, Integer idSanPham);

    @Query(value = """
        SELECT 
            sp.id,
            sp.ma_san_pham,
            sp.ten_san_pham,
            th.ten_thuong_hieu,
            dm.ten_danh_muc,
            MIN(spct.gia_ban),
            (
                SELECT TOP 1 ha.link 
                FROM hinh_anh ha 
                JOIN san_pham_chi_tiet s ON ha.id_san_pham_chi_tiet = s.id 
                WHERE s.id_san_pham = sp.id AND ha.la_anh_chinh = 1
            ),
            yt.ngay_tao
        FROM yeu_thich yt
        JOIN san_pham sp ON yt.id_san_pham = sp.id
        JOIN thuong_hieu th ON sp.id_thuong_hieu = th.id
        JOIN danh_muc dm ON sp.id_danh_muc = dm.id
        LEFT JOIN san_pham_chi_tiet spct ON sp.id = spct.id_san_pham
        WHERE yt.id_khach_hang = :idKhachHang
        GROUP BY sp.id, sp.ma_san_pham, sp.ten_san_pham, th.ten_thuong_hieu, dm.ten_danh_muc, yt.ngay_tao
        """, nativeQuery = true)
    List<Object[]> getDanhSachYeuThichRaw(@Param("idKhachHang") Integer idKhachHang);
}
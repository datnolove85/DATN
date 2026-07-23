package com.example.backend.Repository;

import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Request.HoaDonChiTietWithAnhDTO;
import com.example.backend.Response.thongke.TopProductResponse;
import com.example.backend.Response.thongke.TopProductStatistic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    List<HoaDonChiTiet> findByIdHoaDon_Id(Integer idHoaDon);

    Optional<HoaDonChiTiet> findByIdHoaDon_IdAndIdSanPhamChiTiet_IdAndDonGia(
            Integer idHoaDon,
            Integer idSanPhamChiTiet,
            BigDecimal donGia
    );

    @Query("""

            SELECT ct
FROM HoaDonChiTiet ct
WHERE ct.idHoaDon.id = :idHoaDon
""")
    List<HoaDonChiTiet> findByHoaDon(@Param("idHoaDon") Integer idHoaDon);

    Optional<HoaDonChiTiet> findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(
            Integer idHoaDon,
            Integer idSanPhamChiTiet
    );

    List<HoaDonChiTiet> findByIdSanPhamChiTiet_Id(
            Integer idSanPhamChiTiet
    );
    // Bổ sung hoặc thay thế method topProducts:
    @Query("""
            SELECT new com.example.backend.Response.thongke.TopProductStatistic(
                sp.id,
                sp.maSanPham,
                sp.tenSanPham,
                th.tenThuongHieu,
                cl.tenChatLieu,
                SUM(hct.soLuong),
                SUM(hct.thanhTien)
            )
            FROM HoaDonChiTiet hct
            JOIN hct.idHoaDon hd
            JOIN hct.idSanPhamChiTiet sct
            JOIN sct.idSanPham sp
            LEFT JOIN sp.idThuongHieu th
            LEFT JOIN sp.idChatLieu cl
            WHERE hd.trangThai = 'hoan_thanh'
            AND hd.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR hd.loaiHoaDon = :loaiHoaDon)
            GROUP BY sp.id, sp.maSanPham, sp.tenSanPham, th.tenThuongHieu, cl.tenChatLieu
            ORDER BY SUM(hct.soLuong) DESC
            """)
    List<TopProductStatistic> topProductsTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("loaiHoaDon") String loaiHoaDon,
            Pageable pageable
    );
   }

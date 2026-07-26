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

    @Query("""
            SELECT hdct
            FROM HoaDonChiTiet hdct
            WHERE hdct.idSanPhamChiTiet.id = :spctId
            AND hdct.idHoaDon.trangThai = 'cho_xac_nhan'
            """)
    List<HoaDonChiTiet> findChoXacNhanBySpct(Integer spctId);

    @Query("""
            SELECT new com.example.backend.Response.thongke.TopProductStatistic(
                sp.id,
                sp.maSanPham,
                th.tenThuongHieu,
                cl.tenChatLieu,
                sp.tenSanPham,
                SUM(hct.soLuong),
            SUM(hct.thanhTien),
            SUM(hct.soLuong * sct.giaNhap)
            )
            FROM HoaDonChiTiet hct
            JOIN hct.idHoaDon hd
            JOIN ThanhToan tt
                ON hd.id = tt.idHoaDon.id
                AND tt.trangThai = 'da_thanh_toan'
            JOIN hct.idSanPhamChiTiet sct
            JOIN sct.idSanPham sp
            LEFT JOIN sp.idThuongHieu th
            LEFT JOIN sp.idChatLieu cl
            WHERE hd.trangThai = 'hoan_thanh'
            AND hd.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR hd.loaiHoaDon = :loaiHoaDon)
            GROUP BY
                sp.id,
                sp.maSanPham,
                th.tenThuongHieu,
                cl.tenChatLieu,
                sp.tenSanPham
            ORDER BY SUM(hct.soLuong) DESC
            """)
    List<TopProductStatistic> topProductsTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("loaiHoaDon") String loaiHoaDon,
            Pageable pageable
    );

    @Query("""
                SELECT COALESCE(SUM(hct.soLuong * hct.giaNhap),0)
                FROM HoaDonChiTiet hct
                JOIN hct.idHoaDon hd
                WHERE hd.trangThai = 'hoan_thanh'
                AND hd.ngayTao BETWEEN :from AND :to
                AND (:loaiHoaDon IS NULL
                     OR :loaiHoaDon = ''
                     OR hd.loaiHoaDon = :loaiHoaDon)
            """)
    BigDecimal tongGiaVonTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("loaiHoaDon") String loaiHoaDon
    );

    @Query("""
            SELECT COALESCE(SUM(hct.soLuong),0)
            FROM HoaDonChiTiet hct
            JOIN hct.idHoaDon hd
            WHERE hd.trangThai='hoan_thanh'
            AND hd.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL
                 OR :loaiHoaDon=''
                 OR hd.loaiHoaDon=:loaiHoaDon)
            """)
    Long tongSoLuongBanTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("loaiHoaDon") String loaiHoaDon);

    @Query("""
            SELECT COUNT(DISTINCT hct.idSanPhamChiTiet.id)
            FROM HoaDonChiTiet hct
            JOIN hct.idHoaDon hd
            WHERE hd.trangThai='hoan_thanh'
            AND hd.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL
                 OR :loaiHoaDon=''
                 OR hd.loaiHoaDon=:loaiHoaDon)
            """)
    Long countSkuDaBanTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("loaiHoaDon") String loaiHoaDon);

    List<HoaDonChiTiet> findByIdSanPhamChiTiet_IdAndIdHoaDon_TrangThai(Integer idSpct, String trangThai);
}

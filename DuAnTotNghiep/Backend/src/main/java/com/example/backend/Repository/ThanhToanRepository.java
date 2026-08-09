package com.example.backend.Repository;

import com.example.backend.Entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {


    @Query("""
                select p.tenPhuongThuc
                from ThanhToan tt
                join tt.idPhuongThucThanhToan p
                where tt.idHoaDon.id = :hoaDonId
            """)
    String findPhuongThucByHoaDonId(Integer hoaDonId);

    Optional<ThanhToan> findFirstByIdHoaDon_Id(Integer idHoaDon);

    @Query("""
            SELECT COALESCE(SUM(tt.soTien),0)
            FROM ThanhToan tt
            JOIN tt.idHoaDon hd
            JOIN tt.idPhuongThucThanhToan pt
            WHERE hd.trangThai='hoan_thanh'
            AND pt.maPhuongThuc='CASH'
            AND hd.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR hd.loaiHoaDon = :loaiHoaDon)
            """)
    BigDecimal tongTienMatTheoKhoang(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("loaiHoaDon") String loaiHoaDon);

    @Query("""
            SELECT COALESCE(SUM(tt.soTien),0)
            FROM ThanhToan tt
            JOIN tt.idHoaDon hd
            JOIN tt.idPhuongThucThanhToan pt
            WHERE hd.trangThai='hoan_thanh'
            AND pt.maPhuongThuc <> 'CASH'
            AND hd.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR hd.loaiHoaDon = :loaiHoaDon)
            """)
    BigDecimal tongChuyenKhoanTheoKhoang(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("loaiHoaDon") String loaiHoaDon);

    ThanhToan findByIdHoaDon_Id(Integer idHoaDon);

    @Query("SELECT t FROM ThanhToan t WHERE t.idHoaDon.id = :idHoaDon")
    List<ThanhToan> getDanhSachThanhToanTheoHoaDon(Integer idHoaDon);

    Optional<ThanhToan> findFirstByIdHoaDon_IdOrderByNgayThanhToanDesc(Integer idHoaDon);

}

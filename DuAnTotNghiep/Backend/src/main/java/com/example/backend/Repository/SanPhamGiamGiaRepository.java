package com.example.backend.Repository;

import com.example.backend.Entity.SanPhamGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamGiamGiaRepository
        extends JpaRepository<SanPhamGiamGia, Integer> {


    /**
     * Đếm số SPCT đang nằm trong đợt giảm giá
     */
    long countByDotGiamGiaId(Integer idDot);


    /**
     * Kiểm tra SPCT đã có trong đợt giảm giá chưa
     */
    boolean existsByDotGiamGiaIdAndSanPhamChiTietId(
            Integer idDot,
            Integer idSanPhamChiTiet
    );


    /**
     * Lấy danh sách SPCT giảm giá theo đợt
     */
    List<SanPhamGiamGia> findByDotGiamGiaId(
            Integer idDot
    );


    /**
     * Xóa SPCT khỏi đợt giảm giá
     */
    void deleteByDotGiamGiaIdAndSanPhamChiTietId(
            Integer idDot,
            Integer idSanPhamChiTiet
    );

    @Query("""
            SELECT spgg
            FROM SanPhamGiamGia spgg
            JOIN FETCH spgg.dotGiamGia dgg
            WHERE dgg.trangThai = 'dang_dien_ra'
            AND CURRENT_TIMESTAMP BETWEEN dgg.ngayBatDau AND dgg.ngayKetThuc
            """)
    List<SanPhamGiamGia> findAllDangGiamGia();

    @Query("""
        SELECT spgg
        FROM SanPhamGiamGia spgg
        JOIN FETCH spgg.dotGiamGia dgg
        WHERE spgg.sanPhamChiTiet.id = :idSpct
        AND dgg.trangThai = 'dang_dien_ra'
        AND CURRENT_TIMESTAMP BETWEEN dgg.ngayBatDau AND dgg.ngayKetThuc
    """)
    Optional<SanPhamGiamGia> findDangGiamGiaBySpctId(
            @Param("idSpct") Integer idSpct
    );
}
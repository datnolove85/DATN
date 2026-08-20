package com.example.backend.Repository;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Response.HoaDonResponse;
import com.example.backend.Response.thongke.OrderStatusResponse;
import com.example.backend.Response.thongke.TopCustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer>, JpaSpecificationExecutor<HoaDon> {
    @Query(value = "SELECT * FROM v_hoa_don_full", nativeQuery = true)
    List<Object[]> getAllFromView();

    // Spring Boot tự động cung cấp findAll(), findAll(Pageable pageable),...
    @Query("""
            select new com.example.backend.Response.HoaDonResponse(
                hd.id,
                hd.idKhachHang.id,
                hd.maHoaDon,
                hd.tongTienHang,
                hd.tongGiamGia,
                hd.phiVanChuyen,
                hd.tongThanhToan,
                hd.tenNguoiNhan,
                hd.soDienThoaiNguoiNhan,
                hd.diaChiGiaoHang,
                hd.loaiHoaDon,
                hd.trangThai,
                hd.ghiChu,
                hd.ngayTao,
                hd.ngayCapNhat,
                hd.trangThaiThanhToan,
                p.tenPhuongThuc
            )
            from HoaDon hd
            left join ThanhToan tt on tt.idHoaDon.id = hd.id
            left join PhuongThucThanhToan p on p.id = tt.idPhuongThucThanhToan.id
            """)
    List<HoaDonResponse> getAllResponse();


    List<HoaDon> findByLoaiHoaDonAndTrangThaiOrderByNgayTaoDesc(
            String loaiHoaDon,
            String trangThai
    );

    @Query(
            value = """
                    select new com.example.backend.Response.HoaDonResponse(
                        hd.id,
                        hd.idKhachHang.id,
                        hd.maHoaDon,
                        hd.tongTienHang,
                        hd.tongGiamGia,
                        hd.phiVanChuyen,
                        hd.tongThanhToan,
                        hd.tenNguoiNhan,
                        hd.soDienThoaiNguoiNhan,
                        hd.diaChiGiaoHang,
                        hd.loaiHoaDon,
                        hd.trangThai,
                        hd.ghiChu,
                        hd.ngayTao,
                        hd.ngayCapNhat,
                        hd.trangThaiThanhToan,
                        p.tenPhuongThuc
                    )
                    from HoaDon hd
                    left join ThanhToan tt on tt.idHoaDon.id = hd.id
                    left join PhuongThucThanhToan p on p.id = tt.idPhuongThucThanhToan.id
                    where hd.loaiHoaDon = 'online'
                    and (
                        lower(hd.maHoaDon) like lower(concat('%', :keyword, '%'))
                        or lower(hd.tenNguoiNhan) like lower(concat('%', :keyword, '%'))
                        or lower(hd.soDienThoaiNguoiNhan) like lower(concat('%', :keyword, '%'))
                    )
                    """,
            countQuery = """
                    select count(hd)
                    from HoaDon hd
                    where hd.loaiHoaDon = 'online'
                    and (
                        lower(hd.maHoaDon) like lower(concat('%', :keyword, '%'))
                        or lower(hd.tenNguoiNhan) like lower(concat('%', :keyword, '%'))
                        or lower(hd.soDienThoaiNguoiNhan) like lower(concat('%', :keyword, '%'))
                    )
                    """
    )
    Page<HoaDonResponse> searchOnline(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    List<HoaDon> findByLoaiHoaDonAndTrangThaiAndIdNhanVien_IdOrderByNgayTaoDesc(
            String loaiHoaDon,
            String trangThai,
            Integer idNhanVien
    );

    @Query("""
            SELECT h
            FROM HoaDon h
            WHERE h.trangThai='cho_xac_nhan'
            AND h.ngayCapNhat <= :time
            """)
    List<HoaDon> findExpiredHoaDon(LocalDateTime time);

    @Query("""
    SELECT h FROM HoaDon h
    WHERE h.trangThai = 'cho_xac_nhan'
      AND h.loaiHoaDon = :loaiHoaDon
      AND h.ngayTao <= :expiredTime
""")
    List<HoaDon> findExpiredHoaDonByLoai(
            @Param("loaiHoaDon") String loaiHoaDon,
            @Param("expiredTime") LocalDateTime expiredTime
    );

    List<HoaDon> findByIdKhachHang_Id(Integer idKhachHang);


    //Thống kê
    @Query("""
            SELECT COALESCE(SUM(h.tongThanhToan),0)
            FROM HoaDon h
            WHERE h.trangThai='hoan_thanh'
            AND h.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR h.loaiHoaDon = :loaiHoaDon)
            """)
    BigDecimal getTongDoanhThuTheoKhoang(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("loaiHoaDon") String loaiHoaDon);

    // Thêm tham số loaiHoaDon vào đếm đơn hàng
    @Query("""
            SELECT COUNT(h)
            FROM HoaDon h
            WHERE h.trangThai='hoan_thanh'
            AND h.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR h.loaiHoaDon = :loaiHoaDon)
            """)
    Long countHoaDonTheoKhoang(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("loaiHoaDon") String loaiHoaDon);

    // Thêm điều kiện lọc cho biểu đồ doanh thu theo ngày
    @Query(value = """
            SELECT
                FORMAT(h.ngay_tao,'dd/MM') AS label,
                SUM(h.tong_thanh_toan) AS revenue
            FROM hoa_don h
            WHERE h.trang_thai='hoan_thanh'
            AND h.ngay_tao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR h.loai_hoa_don = :loaiHoaDon)
            GROUP BY FORMAT(h.ngay_tao,'dd/MM'), CAST(h.ngay_tao AS DATE)
            ORDER BY CAST(h.ngay_tao AS DATE)
            """, nativeQuery = true)
    List<Object[]> revenueByDay(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("loaiHoaDon") String loaiHoaDon
    );

    // Thêm điều kiện lọc cho biểu đồ doanh thu theo tháng
    @Query(value = """
            SELECT
                FORMAT(h.ngay_tao,'MM/yyyy') AS label,
                SUM(h.tong_thanh_toan) AS revenue
            FROM hoa_don h
            WHERE h.trang_thai='hoan_thanh'
            AND h.ngay_tao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR h.loai_hoa_don = :loaiHoaDon)
            GROUP BY YEAR(h.ngay_tao),MONTH(h.ngay_tao),FORMAT(h.ngay_tao,'MM/yyyy')
            ORDER BY YEAR(h.ngay_tao),MONTH(h.ngay_tao)
            """, nativeQuery = true)
    List<Object[]> revenueByMonth(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("loaiHoaDon") String loaiHoaDon
    );

    @Query(value = """
            SELECT
                CAST(YEAR(h.ngay_tao) AS VARCHAR) AS label,
                SUM(h.tong_thanh_toan) AS revenue
            FROM hoa_don h
            WHERE h.trang_thai='hoan_thanh'
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR h.loai_hoa_don = :loaiHoaDon)
            GROUP BY YEAR(h.ngay_tao)
            ORDER BY YEAR(h.ngay_tao)
            """, nativeQuery = true)
    List<Object[]> revenueByYear(@Param("loaiHoaDon") String loaiHoaDon);

    // Thêm điều kiện lọc cho thống kê trạng thái đơn hàng
    @Query("""
            SELECT new com.example.backend.Response.thongke.OrderStatusResponse(
                h.trangThai,
                COUNT(h)
            )
            FROM HoaDon h
            WHERE h.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR h.loaiHoaDon = :loaiHoaDon)
            GROUP BY h.trangThai
            ORDER BY
            CASE h.trangThai
                WHEN 'cho_xac_nhan' THEN 1
                WHEN 'da_xac_nhan' THEN 2
                WHEN 'dang_giao' THEN 3
                WHEN 'da_giao' THEN 4
                WHEN 'hoan_thanh' THEN 5
                WHEN 'da_huy' THEN 6
            END
            """)
    List<OrderStatusResponse> thongKeTrangThaiTheoKhoang(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("loaiHoaDon") String loaiHoaDon);

    // Thêm điều kiện lọc cho Top khách hàng
    @Query("""
            SELECT new com.example.backend.Response.thongke.TopCustomerResponse(
                kh.id,
                kh.hoTen,
                COUNT(hd),
                SUM(hd.tongThanhToan)
            )
            FROM HoaDon hd
            JOIN hd.idKhachHang kh
            WHERE hd.trangThai='hoan_thanh'
            AND hd.ngayTao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR hd.loaiHoaDon = :loaiHoaDon)
            GROUP BY kh.id, kh.hoTen
            ORDER BY SUM(hd.tongThanhToan) DESC
            """)
    List<TopCustomerResponse> topCustomersTheoKhoang(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, Pageable pageable, @Param("loaiHoaDon") String loaiHoaDon);

    @Query(value = """
            SELECT COALESCE(SUM(hct.so_luong * sct.gia_nhap), 0)
            FROM hoa_don_chi_tiet hct
            JOIN hoa_don h ON h.id = hct.id_hoa_don
            JOIN san_pham_chi_tiet sct ON sct.id = hct.id_san_pham_chi_tiet
            WHERE h.trang_thai = 'hoan_thanh'
            AND h.ngay_tao BETWEEN :from AND :to
            AND (:loaiHoaDon IS NULL OR :loaiHoaDon = '' OR h.loai_hoa_don = :loaiHoaDon)
            """, nativeQuery = true)
    BigDecimal getTongGiaVonTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("loaiHoaDon") String loaiHoaDon
    );

    @Query("""
            SELECT COUNT(h)
            FROM HoaDon h
            WHERE h.trangThai='hoan_thanh'
            AND h.loaiHoaDon='online'
            AND h.ngayTao BETWEEN :from AND :to
            """)
    Long countHoaDonOnlineTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("""
            SELECT COUNT(h)
            FROM HoaDon h
            WHERE h.trangThai='hoan_thanh'
            AND h.loaiHoaDon='tai_quay'
            AND h.ngayTao BETWEEN :from AND :to
            """)
    Long countHoaDonTaiQuayTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("""
            SELECT COUNT(h)
            FROM HoaDon h
            WHERE h.trangThai='hoan_thanh'
            AND h.idKhachHang IS NULL
            AND h.ngayTao BETWEEN :from AND :to
            """)
    Long countKhachLeTheoKhoang(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("SELECT DISTINCT hd FROM HoaDonChiTiet hdct " +
            "JOIN hdct.idHoaDon hd " +
            "JOIN hdct.idSanPhamChiTiet spct " +
            "JOIN spct.idSanPham sp " +
            "WHERE (LOWER(spct.maSanPhamChiTiet) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(spct.tenSanPhamChiTiet) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(sp.maSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(sp.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND hd.trangThai NOT IN ('da_huy', 'hoan_thanh', 'giao_thanh_cong')")
    Page<HoaDon> findDonHangChuaGiaoBySanPhamKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm các hóa đơn theo danh sách Integer ID
    List<HoaDon> findByIdIn(List<Integer> ids);

    @Query("SELECT h FROM HoaDon h WHERE h.loaiHoaDon = 'online' AND h.trangThai = 'cho_xac_nhan' AND h.ngayTao < :expiredTime AND NOT EXISTS (SELECT t FROM ThanhToan t WHERE t.idHoaDon.id = h.id)")
    List<HoaDon> findOnlineWithoutPayment(@Param("expiredTime") LocalDateTime expiredTime);

    @Query("SELECT h FROM HoaDon h WHERE h.loaiHoaDon = 'online' AND h.trangThai = 'cho_xac_nhan' AND h.ngayTao < :expiredTime AND EXISTS (SELECT t FROM ThanhToan t WHERE t.idHoaDon.id = h.id)")
    List<HoaDon> findOnlineWithPayment(@Param("expiredTime") LocalDateTime expiredTime);


}
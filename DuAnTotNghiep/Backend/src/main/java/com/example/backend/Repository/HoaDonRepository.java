package com.example.backend.Repository;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Response.HoaDonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    List<HoaDon> findByIdKhachHang_Id(Integer idKhachHang);


}
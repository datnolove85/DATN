package com.example.backend.Repository;

import com.example.backend.Entity.Voucher;
import com.example.backend.Response.VoucherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByMaVoucherIgnoreCase(String maVoucher);

    @Query("""
        SELECT NEW com.example.backend.Response.VoucherResponse(
            v.id,
            v.maVoucher,
            v.tenVoucher,
            v.loaiGiamGia,
            v.giaTriGiam,
            v.giaTriDonHangToiThieu,
            v.giaTriGiamToiDa,
            v.soLuong,
            v.soLuongDaDung,
            v.ngayBatDau,
            v.ngayKetThuc,
            v.moTa,
            v.ngayTao,
            v.ngayCapNhat,
            v.trangThai
        )
        FROM Voucher v
        ORDER BY v.id DESC
    """)
    Page<VoucherResponse> phanTrang(Pageable pageable);

    @Query("""
        SELECT NEW com.example.backend.Response.VoucherResponse(
            v.id,
            v.maVoucher,
            v.tenVoucher,
            v.loaiGiamGia,
            v.giaTriGiam,
            v.giaTriDonHangToiThieu,
            v.giaTriGiamToiDa,
            v.soLuong,
            v.soLuongDaDung,
            v.ngayBatDau,
            v.ngayKetThuc,
            v.moTa,
            v.ngayTao,
            v.ngayCapNhat,
            v.trangThai
        )
        FROM Voucher v
        WHERE LOWER(v.maVoucher) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.tenVoucher) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(v.loaiGiamGia) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY v.id DESC
    """)
    List<VoucherResponse> search(@Param("keyword") String keyword);

    @Query("""
        SELECT NEW com.example.backend.Response.VoucherResponse(
            v.id,
            v.maVoucher,
            v.tenVoucher,
            v.loaiGiamGia,
            v.giaTriGiam,
            v.giaTriDonHangToiThieu,
            v.giaTriGiamToiDa,
            v.soLuong,
            v.soLuongDaDung,
            v.ngayBatDau,
            v.ngayKetThuc,
            v.moTa,
            v.ngayTao,
            v.ngayCapNhat,
            v.trangThai
        )
        FROM Voucher v
        WHERE v.id = :id
    """)
    Optional<VoucherResponse> detail(@Param("id") Integer id);

    @Query("select max(v.id) from Voucher v")
    Integer getMaxId();
}
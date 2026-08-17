
        package com.example.backend.Repository;

import com.example.backend.Entity.HoaDonVoucher;
import com.example.backend.Entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonVoucherRepository
        extends JpaRepository<HoaDonVoucher, Integer> {

    Optional<HoaDonVoucher> findFirstByIdHoaDon_Id(Integer idHoaDon);

    Optional<HoaDonVoucher> findByIdHoaDon_Id(Integer idHoaDonId);

    @Query("""
        SELECT v
        FROM HoaDonVoucher hdv
        JOIN hdv.idVoucher v
        WHERE hdv.idHoaDon.id = :idHoaDon
    """)
    Optional<Voucher> findVoucherByHoaDonId(
            @Param("idHoaDon") Integer idHoaDon
    );

    /*
     * Voucher thông thường
     * GIỮ NGUYÊN TÊN METHOD CŨ
     */
    List<HoaDonVoucher> findByIdVoucher_Id(Integer idVoucher);

    /*
     * Voucher đổi từ minigame
     * GIỮ NGUYÊN TÊN METHOD CŨ
     *
     * Lưu ý:
     * idKhoVoucher là tên FIELD trong Entity HoaDonVoucher,
     * không phải tên bảng SQL.
     */
    List<HoaDonVoucher> findByIdKhoVoucher_Id(Integer idKhoVoucher);
}

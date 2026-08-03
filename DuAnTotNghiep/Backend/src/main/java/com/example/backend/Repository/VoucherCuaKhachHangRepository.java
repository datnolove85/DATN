package com.example.backend.Repository;

import com.example.backend.Entity.KhoVoucher;
import com.example.backend.Entity.VoucherCuaKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherCuaKhachHangRepository extends JpaRepository<VoucherCuaKhachHang, Integer> {
    // Lấy danh sách voucher cá nhân theo ID khách hàng
    List<VoucherCuaKhachHang> findByIdKhachHang(Integer idKhachHang);

    @Query("SELECT kv FROM VoucherCuaKhachHang vk " +
            "JOIN KhoVoucher kv ON vk.idKhoVoucher = kv.id " +
            "WHERE vk.idKhachHang = :idKhachHang AND vk.trangThai = :trangThai")
    List<KhoVoucher> findKhoVoucherByKhachHang(
            @Param("idKhachHang") Integer idKhachHang,
            @Param("trangThai") String trangThai
    );



    @Query(value = """
        SELECT
            v.id AS idVoucherKhachHang,
            v.trang_thai AS trangThai,
            v.ngay_doi AS ngayDoi,

            k.id AS idKhoVoucher,
            k.ten_voucher AS tenVoucher,
            k.ma_code AS maCode,
            k.loai_giam_gia AS loaiGiamGia,
            k.gia_tri_giam AS giaTriGiam,
            k.gia_tri_giam_toi_da AS giaTriGiamToiDa,
            k.dieu_kien_toi_thieu AS dieuKienToiThieu,
            k.so_xu_doi AS soXuDoi,
            k.ngay_bat_dau AS ngayBatDau,
            k.ngay_het_han AS ngayHetHan,
            k.mo_ta AS moTa

        FROM voucher_cua_khach_hang v
        JOIN kho_voucher k
             ON v.id_kho_voucher = k.id

        WHERE v.id_khach_hang = :idKhachHang
        ORDER BY v.ngay_doi DESC
        """,
            nativeQuery = true)
    List<Object[]> getVoucherResponse(@Param("idKhachHang") Integer idKhachHang);

    Optional<VoucherCuaKhachHang> findByIdAndTrangThai(
            Integer id,
            String trangThai
    );

    @Query("""
SELECT vckh, kv
FROM VoucherCuaKhachHang vckh
JOIN KhoVoucher kv
ON kv.id = vckh.idKhoVoucher
WHERE vckh.idKhachHang = :idKhachHang
AND vckh.trangThai = 'CHUA_DUNG'
""")
    List<Object[]> findVoucherByKhachHang(@Param("idKhachHang") Integer idKhachHang);


    List<VoucherCuaKhachHang> findByIdKhachHangAndTrangThai(
            Integer idKhachHang,
            String trangThai
    );
}
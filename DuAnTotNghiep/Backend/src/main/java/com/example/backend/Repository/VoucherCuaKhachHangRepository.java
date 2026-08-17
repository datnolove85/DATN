
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
public interface VoucherCuaKhachHangRepository
        extends JpaRepository<VoucherCuaKhachHang, Integer> {

    // =========================================================
    // Giữ nguyên tên method
    // =========================================================

    // Lấy danh sách voucher cá nhân theo ID khách hàng
    List<VoucherCuaKhachHang> findByIdKhachHang(
            Integer idKhachHang
    );


    // =========================================================
    // Giữ nguyên tên method: findKhoVoucherByKhachHang
    // Chỉ sửa query để dùng KhoVoucher -> bảng voucher_minigame
    // =========================================================

    @Query("""
        SELECT kv
        FROM VoucherCuaKhachHang vk
        JOIN KhoVoucher kv
            ON vk.idVoucherMinigame = kv.id
        WHERE vk.idKhachHang = :idKhachHang
          AND vk.trangThai = :trangThai
    """)
    List<KhoVoucher> findKhoVoucherByKhachHang(
            @Param("idKhachHang") Integer idKhachHang,
            @Param("trangThai") String trangThai
    );


    // =========================================================
    // Giữ nguyên tên method: getVoucherResponse
    // Chỉ sửa tên bảng/cột SQL
    // =========================================================

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
        JOIN voucher_minigame k
             ON v.id_voucher_minigame = k.id

        WHERE v.id_khach_hang = :idKhachHang
          AND v.trang_thai = 'CHUA_DUNG'
          AND k.trang_thai = 1

        ORDER BY v.ngay_doi DESC
        """,
            nativeQuery = true)
    List<Object[]> getVoucherResponse(
            @Param("idKhachHang") Integer idKhachHang
    );


    // =========================================================
    // Giữ nguyên
    // =========================================================

    Optional<VoucherCuaKhachHang> findByIdAndTrangThai(
            Integer id,
            String trangThai
    );


    // =========================================================
    // Giữ nguyên tên method: findVoucherByKhachHang
    // Chỉ sửa query
    // =========================================================

    @Query("""
        SELECT vckh, kv
        FROM VoucherCuaKhachHang vckh
        JOIN KhoVoucher kv
            ON kv.id = vckh.idVoucherMinigame
        WHERE vckh.idKhachHang = :idKhachHang
          AND vckh.trangThai = 'CHUA_DUNG'
    """)
    List<Object[]> findVoucherByKhachHang(
            @Param("idKhachHang") Integer idKhachHang
    );


    // =========================================================
    // Giữ nguyên
    // =========================================================

    List<VoucherCuaKhachHang> findByIdKhachHangAndTrangThai(
            Integer idKhachHang,
            String trangThai
    );
}


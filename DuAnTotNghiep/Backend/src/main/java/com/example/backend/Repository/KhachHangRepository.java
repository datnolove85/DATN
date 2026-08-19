package com.example.backend.Repository;

import com.example.backend.Entity.KhachHang;
import com.example.backend.Response.KhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer>, JpaSpecificationExecutor<KhachHang> {

    @Query("""
                SELECT NEW com.example.backend.Response.KhachHangResponse(
                    kh.id,
                    kh.maKhachHang,
                    kh.hoTen,
                    tk.id,
                    tk.email,
                    tk.tenTaiKhoan,
                    tk.soDienThoai,
                    kh.ngaySinh,
                    kh.anh,
                    dc.thanhPho,
                    dc.quan,
                    dc.phuong,
                    dc.diaChiCuThe,
                    kh.diaChi,
                    kh.gioiTinh,
                    kh.trangThai,
                    kh.hangThanhVien,
                    kh.soLanMua,
                    kh.tongChiTieu,
                    kh.ngayBatDauChuKy,
                    kh.ngayHetHanHang,
                    kh.ngayMuaCuoi,
                    kh.soDuXu,
                    kh.chuoiDiemDanh,
                    kh.ngayDiemDanhGanNhat,
                    kh.soLuotLatThe,
                    kh.ngayTao
                )
                FROM KhachHang kh
                LEFT JOIN kh.idTaiKhoan tk
                LEFT JOIN DiaChiKhachHang dc ON dc.idKhachHang.id = kh.id AND dc.macDinh = true AND dc.trangThai = 1
            """)
    Page<KhachHangResponse> phanTrang(Pageable pageable);

    @Query("""
                SELECT NEW com.example.backend.Response.KhachHangResponse(
                    kh.id,
                    kh.maKhachHang,
                    kh.hoTen,
                    tk.id,
                    tk.email,
                    tk.tenTaiKhoan,
                    tk.soDienThoai,
                    kh.ngaySinh,
                    kh.anh,
                    dc.thanhPho,
                    dc.quan,
                    dc.phuong,
                    dc.diaChiCuThe,
                    kh.diaChi,
                    kh.gioiTinh,
                    kh.trangThai,
                    kh.hangThanhVien,
                    kh.soLanMua,
                    kh.tongChiTieu,
                    kh.ngayBatDauChuKy,
                    kh.ngayHetHanHang,
                    kh.ngayMuaCuoi,
                    kh.soDuXu,
                    kh.chuoiDiemDanh,
                    kh.ngayDiemDanhGanNhat,
                    kh.soLuotLatThe,
                    kh.ngayTao
                )
                FROM KhachHang kh
                LEFT JOIN kh.idTaiKhoan tk
                LEFT JOIN DiaChiKhachHang dc ON dc.idKhachHang.id = kh.id AND dc.macDinh = true AND dc.trangThai = 1
                WHERE kh.id = :id
            """)
    KhachHangResponse detail(@Param("id") Integer id);

    @Query("""
                SELECT NEW com.example.backend.Response.KhachHangResponse(
                    kh.id,
                    kh.maKhachHang,
                    kh.hoTen,
                    tk.id,
                    tk.email,
                    tk.tenTaiKhoan,
                    tk.soDienThoai,
                    kh.ngaySinh,
                    kh.anh,
                    dc.thanhPho,
                    dc.quan,
                    dc.phuong,
                    dc.diaChiCuThe,
                    kh.diaChi,
                    kh.gioiTinh,
                    kh.trangThai,
                    kh.hangThanhVien,
                    kh.soLanMua,
                    kh.tongChiTieu,
                    kh.ngayBatDauChuKy,
                    kh.ngayHetHanHang,
                    kh.ngayMuaCuoi,
                    kh.soDuXu,
                    kh.chuoiDiemDanh,
                    kh.ngayDiemDanhGanNhat,
                    kh.soLuotLatThe,
                    kh.ngayTao
                )
                FROM KhachHang kh
                LEFT JOIN kh.idTaiKhoan tk
                LEFT JOIN DiaChiKhachHang dc ON dc.idKhachHang.id = kh.id AND dc.macDinh = true AND dc.trangThai = 1
                WHERE LOWER(kh.maKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(kh.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(tk.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(tk.tenTaiKhoan) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(tk.soDienThoai) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(dc.thanhPho) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(dc.quan) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(dc.phuong) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(dc.diaChiCuThe) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<KhachHangResponse> search(@Param("keyword") String keyword);

    Optional<KhachHang> findByIdTaiKhoan_Id(Integer idTaiKhoan);

    @Query("""
            SELECT COUNT(k)
            FROM KhachHang k
            """)
    Long countKhachHang();

    @Query("""
            SELECT COUNT(k)
            FROM KhachHang k
            WHERE k.trangThai = true
            """)
    Long countKhachThanhVien();

    @Query(value = """
            SELECT TOP 1 ma_khach_hang
            FROM khach_hang
            WHERE ma_khach_hang LIKE 'KH%'
            ORDER BY ma_khach_hang DESC
            """, nativeQuery = true)
    String findLastMaKhachHang();
}
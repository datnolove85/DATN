package com.example.backend.Repository;

import com.example.backend.Entity.DiemDanhHangNgay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DiemDanhHangNgayRepository extends JpaRepository<DiemDanhHangNgay, Integer> {

    // Kiểm tra xem khách hàng đã điểm danh trong ngày cụ thể đó chưa
    boolean existsByIdKhachHang_IdAndNgayDiemDanh(Integer idKhachHang, LocalDate ngayDiemDanh);

    // Lấy thông tin điểm danh của khách hàng trong một ngày cụ thể
    Optional<DiemDanhHangNgay> findByIdKhachHang_IdAndNgayDiemDanh(Integer idKhachHang, LocalDate ngayDiemDanh);


}
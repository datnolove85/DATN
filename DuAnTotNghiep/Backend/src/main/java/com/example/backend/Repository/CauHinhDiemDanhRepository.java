package com.example.backend.Repository;

import com.example.backend.Entity.CauHinhDiemDanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CauHinhDiemDanhRepository extends JpaRepository<CauHinhDiemDanh, Integer> {

    // Lấy danh sách cấu hình sắp xếp tăng dần theo ngày thứ (1, 2, 3...)
    List<CauHinhDiemDanh> findAllByOrderByNgayThuAsc();

    // Tìm kiếm cấu hình theo ngày thứ
    Optional<CauHinhDiemDanh> findByNgayThu(Integer ngayThu);

    List<CauHinhDiemDanh> findByTrangThaiTrueOrderByNgayThuAsc();
}
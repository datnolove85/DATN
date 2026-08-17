package com.example.backend.Repository;

import com.example.backend.Entity.CauHinhHangThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CauHinhHangThanhVienRepository extends JpaRepository<CauHinhHangThanhVien, Integer> {
    // Lấy danh sách hạng sắp xếp theo ngưỡng chi tiêu tăng dần để dễ xét hạng
    List<CauHinhHangThanhVien> findByTrangThaiTrueOrderByNguongChiTieuToiThieuAsc();
}
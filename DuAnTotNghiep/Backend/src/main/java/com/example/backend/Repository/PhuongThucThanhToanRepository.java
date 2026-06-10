package com.example.backend.Repository;

import com.example.backend.Entity.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan, Integer> {
    Optional<PhuongThucThanhToan> findByMaPhuongThuc(String ma);
}

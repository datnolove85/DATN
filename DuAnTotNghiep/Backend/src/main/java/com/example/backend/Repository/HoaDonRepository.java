package com.example.backend.Repository;

import com.example.backend.Entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    // Spring Boot tự động cung cấp findAll(), findAll(Pageable pageable),...
}

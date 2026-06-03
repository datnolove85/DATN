package com.example.backend.Repository;

import com.example.backend.Entity.DotGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Integer> {

    boolean existsByMaDotGiamGia(String maDotGiamGia);
}
package com.example.backend.Repository;

import com.example.backend.Entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang,Integer> {

    Optional<GioHang> findByIdKhachHang_Id(Integer idKhachHang);

}
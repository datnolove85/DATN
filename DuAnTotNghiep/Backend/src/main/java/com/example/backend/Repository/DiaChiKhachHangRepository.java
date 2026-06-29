package com.example.backend.Repository;

import com.example.backend.Entity.DiaChiKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiaChiKhachHangRepository extends JpaRepository<DiaChiKhachHang, Integer> {

    Optional<DiaChiKhachHang> findByIdKhachHang_IdAndMacDinhTrue(Integer idKhachHang);


    Optional<DiaChiKhachHang> findFirstByIdKhachHang_IdAndMacDinhAndTrangThaiOrderByIdDesc(Integer idKhachHang, Boolean macDinh, Integer trangThai);
}
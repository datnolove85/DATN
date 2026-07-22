package com.example.backend.Repository;

import com.example.backend.Entity.TraHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraHangRepository extends JpaRepository<TraHang,Integer> {



    List<TraHang> findByHoaDonId(Integer hoaDonId);
}

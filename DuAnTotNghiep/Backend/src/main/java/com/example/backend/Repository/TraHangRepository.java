package com.example.backend.Repository;

import com.example.backend.Entity.TraHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraHangRepository extends JpaRepository<TraHang,Integer> {

}

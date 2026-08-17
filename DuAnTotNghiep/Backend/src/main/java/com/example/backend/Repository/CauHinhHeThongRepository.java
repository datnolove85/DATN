package com.example.backend.Repository;

import com.example.backend.Entity.CauHinhHeThong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CauHinhHeThongRepository extends JpaRepository<CauHinhHeThong, Integer> {
    Optional<CauHinhHeThong> findByMaCauHinh(String maCauHinh);
}
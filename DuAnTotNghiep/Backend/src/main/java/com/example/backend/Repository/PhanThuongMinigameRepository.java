package com.example.backend.Repository;

import com.example.backend.Entity.PhanThuongMinigame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhanThuongMinigameRepository extends JpaRepository<PhanThuongMinigame, Integer> {
    List<PhanThuongMinigame> findByLoaiGameAndTrangThai(String loaiGame, Boolean trangThai);


}
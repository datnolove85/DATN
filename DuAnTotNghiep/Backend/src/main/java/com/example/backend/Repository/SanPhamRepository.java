package com.example.backend.Repository;

import com.example.backend.Entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham,Integer> {
    @Query("""
        select sp
        from SanPham sp
        where sp.trangThai = true
    """)
    List<SanPham> getAllClient();
}

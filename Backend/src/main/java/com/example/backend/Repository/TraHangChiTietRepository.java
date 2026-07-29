package com.example.backend.Repository;

import com.example.backend.Entity.TraHangChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TraHangChiTietRepository
        extends JpaRepository<TraHangChiTiet,Integer> {

    @Query("""
            select coalesce(sum(thct.soLuongTra),0)
            from TraHangChiTiet thct
            where thct.hoaDonChiTiet.id = :hdctId
            """)
    Integer tongDaTra(Integer hdctId);

}
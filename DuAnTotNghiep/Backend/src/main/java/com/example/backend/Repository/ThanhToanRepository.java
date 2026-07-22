package com.example.backend.Repository;

import com.example.backend.Entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {



        @Query("""
        select p.tenPhuongThuc
        from ThanhToan tt
        join tt.idPhuongThucThanhToan p
        where tt.idHoaDon.id = :hoaDonId
    """)
        String findPhuongThucByHoaDonId(Integer hoaDonId);

        Optional<ThanhToan> findFirstByIdHoaDon_Id(Integer idHoaDon);



}

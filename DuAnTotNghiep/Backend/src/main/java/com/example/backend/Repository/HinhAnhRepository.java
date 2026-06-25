package com.example.backend.Repository;

import com.example.backend.Entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    List<HinhAnh> findByIdSanPhamChiTiet_IdAndTrangThaiTrue(Integer id);

    List<HinhAnh> findAllByIdIn(List<Integer> ids);


}

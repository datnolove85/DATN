package com.example.backend.Repository;

import com.example.backend.Entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    List<HinhAnh> findByIdSanPhamChiTiet_IdAndTrangThaiTrue(Integer id);

    List<HinhAnh> findAllByIdIn(List<Integer> ids);

    Optional<HinhAnh> findFirstByIdSanPhamChiTiet_IdAndLaAnhChinhTrue(Integer idSanPhamChiTiet);
}

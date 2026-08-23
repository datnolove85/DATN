package com.example.backend.Repository;

import com.example.backend.Entity.YeuThich;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YeuThichRepository extends JpaRepository<YeuThich, Integer> {

    // Lấy danh sách sản phẩm yêu thích theo mã khách hàng
    List<YeuThich> findByKhachHangId(Integer idKhachHang);

    // Tìm kiếm bản ghi yêu thích cụ thể của 1 khách cho 1 sản phẩm
    Optional<YeuThich> findByKhachHangIdAndSanPhamId(Integer idKhachHang, Integer idSanPham);

    // Kiểm tra nhanh xem khách đã thích sản phẩm này chưa
    boolean existsByKhachHangIdAndSanPhamId(Integer idKhachHang, Integer idSanPham);
}
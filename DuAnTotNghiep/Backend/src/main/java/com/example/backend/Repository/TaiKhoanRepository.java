package com.example.backend.Repository;

import com.example.backend.Entity.TaiKhoan;
import com.example.backend.Response.TaiKhoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    @Query("""
        SELECT NEW com.example.backend.Response.TaiKhoanResponse(
            tk.id,
            tk.idVaiTro.id,
            tk.idVaiTro.tenVaiTro,
            tk.email,
            tk.soDienThoai,
            tk.matKhau,
            tk.ngayTao,
            tk.ngayCapNhat,
            null,
            null,
            tk.trangThai
        )
        FROM TaiKhoan tk
    """)
    Page<TaiKhoanResponse> phanTrang(Pageable pageable);

    Optional<TaiKhoan> findByEmail(String email);

    Optional<TaiKhoan> findBySoDienThoai(String soDienThoai);

    Optional<TaiKhoan> findByTenTaiKhoan(String tenTaiKhoan);
}

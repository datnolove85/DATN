package com.example.backend.Repository;

import com.example.backend.Entity.VaiTro;
import com.example.backend.Response.VaiTroResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Integer> {
       @Query("""
       SELECT NEW com.example.backend.Response.VaiTroResponse(vt.id,vt.maVaiTro,vt.tenVaiTro,vt.trangThai) 
       FROM VaiTro vt
""")
       Page<VaiTroResponse> phanTrang(Pageable pageable);
       @Query("""
       SELECT NEW com.example.backend.Response.VaiTroResponse(vt.id,vt.maVaiTro,vt.tenVaiTro,vt.trangThai) 
       FROM VaiTro vt
""")
       List<VaiTroResponse> hienThi();

    Optional<VaiTro> findByTenVaiTro(String tenVaiTro);}

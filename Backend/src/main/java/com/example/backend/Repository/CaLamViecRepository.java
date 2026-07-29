package com.example.backend.Repository;

import com.example.backend.Entity.CaLamViec;
import com.example.backend.Response.CaLamViecResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CaLamViecRepository extends JpaRepository<CaLamViec, Integer> {
      @Query("""
      select new com.example.backend.Response.CaLamViecResponse(clv.id,clv.maCa,clv.tenCa,clv.gioBatDau,clv.gioKetThuc,clv.soGio,clv.moTa,clv.ngayTao,clv.nguoiTao,clv.trangThai)
      from CaLamViec clv
""")
    Page<CaLamViecResponse> phanTrang(Pageable pageable);
    @Query("""
      select new com.example.backend.Response.CaLamViecResponse(clv.id,clv.maCa,clv.tenCa,clv.gioBatDau,clv.gioKetThuc,clv.soGio,clv.moTa,clv.ngayTao,clv.nguoiTao,clv.trangThai)
      from CaLamViec clv
      Where clv.id=?1
""")
    CaLamViecResponse detail(Integer id);
}

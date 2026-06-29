package com.example.backend.Repository;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.HoaDonChiTiet;
import com.example.backend.Response.HoaDonResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer>, JpaSpecificationExecutor<HoaDon> {
    @Query(value = "SELECT * FROM v_hoa_don_full", nativeQuery = true)
    List<Object[]> getAllFromView();
    // Spring Boot tự động cung cấp findAll(), findAll(Pageable pageable),...
    @Query("""
select new com.example.backend.Response.HoaDonResponse(
    hd.id,
    hd.idKhachHang.id,
    hd.maHoaDon,
    hd.tongTienHang,
    hd.tongGiamGia,
    hd.phiVanChuyen,
    hd.tongThanhToan,
    hd.tenNguoiNhan,
    hd.soDienThoaiNguoiNhan,
    hd.diaChiGiaoHang,
    hd.loaiHoaDon,
    hd.trangThai,
    hd.ghiChu,
    hd.ngayTao,
    hd.ngayCapNhat,
    p.tenPhuongThuc
)
from HoaDon hd
left join ThanhToan tt on tt.idHoaDon.id = hd.id
left join PhuongThucThanhToan p on p.id = tt.idPhuongThucThanhToan.id
""")
    List<HoaDonResponse> getAllResponse();


    List<HoaDon> findByTrangThaiOrderByNgayTaoDesc(
            String trangThai
    );

}
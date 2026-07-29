package com.example.backend.Service;

import com.example.backend.Entity.DiaChiKhachHang;
import com.example.backend.Entity.KhachHang;
import com.example.backend.Repository.DiaChiKhachHangRepository;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.Request.DiaChiKhachHangRequest;
import com.example.backend.secutity.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaChiKhachHangService {

    private final JwtService jwtService;

    private final KhachHangRepository khachHangRepository;

    private final DiaChiKhachHangRepository diaChiRepository;

    public DiaChiKhachHang getDiaChiMacDinh(HttpServletRequest request){

        KhachHang khachHang = getKhachHangFromToken(request);

        return diaChiRepository
                .findByIdKhachHang_IdAndMacDinhTrue(khachHang.getId())
                .orElseThrow(() -> new RuntimeException("Chưa có địa chỉ mặc định"));
    }
    private KhachHang getKhachHangFromToken(HttpServletRequest request){

        String auth = request.getHeader("Authorization");

        if(auth == null || !auth.startsWith("Bearer ")){
            throw new RuntimeException("Thiếu token");
        }

        String token = auth.substring(7);

        Integer idTaiKhoan = jwtService.extractId(token);

        return khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
    }

    public List<DiaChiKhachHang> getAll(HttpServletRequest request){

        KhachHang khachHang = getKhachHangFromToken(request);

        return diaChiRepository.findByIdKhachHang_IdOrderByMacDinhDescIdDesc(khachHang.getId());

    }
    public DiaChiKhachHang add(DiaChiKhachHangRequest request,
                               HttpServletRequest servletRequest) {

        KhachHang khachHang = getKhachHangFromToken(servletRequest);

        if (Boolean.TRUE.equals(request.getMacDinh())) {

            diaChiRepository.findByIdKhachHang_IdAndMacDinhTrue(khachHang.getId())
                    .ifPresent(dc -> {
                        dc.setMacDinh(false);
                        diaChiRepository.save(dc);
                    });

        }

        DiaChiKhachHang diaChi = new DiaChiKhachHang();

        diaChi.setMaDiaChi("DC" + System.currentTimeMillis());
        diaChi.setTenNguoiNhan(request.getTenNguoiNhan());
        diaChi.setSoDienThoai(request.getSoDienThoai());
        diaChi.setThanhPho(request.getThanhPho());
        diaChi.setQuan(request.getQuan());
        diaChi.setPhuong(request.getPhuong());
        diaChi.setDiaChiCuThe(request.getDiaChiCuThe());
        diaChi.setMacDinh(request.getMacDinh());
        diaChi.setTrangThai(1);
        diaChi.setDistrictId(request.getDistrictId());
        diaChi.setWardCode(request.getWardCode());
        diaChi.setLatitude(request.getLatitude());
        diaChi.setLongitude(request.getLongitude());
        diaChi.setIdKhachHang(khachHang);

        return diaChiRepository.save(diaChi);

    }
    public DiaChiKhachHang update(Integer id,
                                  DiaChiKhachHangRequest request,
                                  HttpServletRequest servletRequest) {

        KhachHang khachHang = getKhachHangFromToken(servletRequest);

        DiaChiKhachHang diaChi = diaChiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));

        if (!diaChi.getIdKhachHang().getId().equals(khachHang.getId())) {
            throw new RuntimeException("Bạn không có quyền sửa địa chỉ này");
        }

        if (Boolean.TRUE.equals(request.getMacDinh())) {

            diaChiRepository.findByIdKhachHang_IdAndMacDinhTrue(khachHang.getId())
                    .ifPresent(dc -> {

                        if (!dc.getId().equals(diaChi.getId())) {
                            dc.setMacDinh(false);
                            diaChiRepository.save(dc);
                        }

                    });

        }

        diaChi.setTenNguoiNhan(request.getTenNguoiNhan());
        diaChi.setSoDienThoai(request.getSoDienThoai());
        diaChi.setThanhPho(request.getThanhPho());
        diaChi.setQuan(request.getQuan());
        diaChi.setPhuong(request.getPhuong());
        diaChi.setDiaChiCuThe(request.getDiaChiCuThe());
        diaChi.setMacDinh(request.getMacDinh());
        diaChi.setDistrictId(request.getDistrictId());
        diaChi.setWardCode(request.getWardCode());

        diaChi.setLatitude(request.getLatitude());
        diaChi.setLongitude(request.getLongitude());
        return diaChiRepository.save(diaChi);

    }
    public void delete(Integer id,
                       HttpServletRequest servletRequest) {

        KhachHang khachHang = getKhachHangFromToken(servletRequest);

        DiaChiKhachHang diaChi = diaChiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));

        if (!diaChi.getIdKhachHang().getId().equals(khachHang.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa địa chỉ này");
        }

        diaChiRepository.delete(diaChi);

    }

    public void doiMacDinh(Integer id,
                           HttpServletRequest servletRequest) {

        KhachHang khachHang = getKhachHangFromToken(servletRequest);

        DiaChiKhachHang diaChi = diaChiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));

        if (!diaChi.getIdKhachHang().getId().equals(khachHang.getId())) {
            throw new RuntimeException("Bạn không có quyền");
        }

        diaChiRepository.findByIdKhachHang_IdAndMacDinhTrue(khachHang.getId())
                .ifPresent(dc -> {

                    dc.setMacDinh(false);

                    diaChiRepository.save(dc);

                });

        diaChi.setMacDinh(true);

        diaChiRepository.save(diaChi);

    }
}
package com.example.backend.Service;

import com.example.backend.Entity.DiaChiKhachHang;
import com.example.backend.Entity.KhachHang;
import com.example.backend.Repository.DiaChiKhachHangRepository;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.secutity.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaChiKhachHangService {

    private final JwtService jwtService;

    private final KhachHangRepository khachHangRepository;

    private final DiaChiKhachHangRepository diaChiRepository;

    public DiaChiKhachHang getDiaChiMacDinh(HttpServletRequest request){

        String auth = request.getHeader("Authorization");

        if(auth == null || !auth.startsWith("Bearer ")){
            throw new RuntimeException("Thiếu token");
        }

        String token = auth.substring(7);

        Integer idTaiKhoan = jwtService.extractId(token);

        KhachHang khachHang = khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        return diaChiRepository
                .findByIdKhachHang_IdAndMacDinhTrue(khachHang.getId())
                .orElseThrow(() -> new RuntimeException("Chưa có địa chỉ mặc định"));
    }

}
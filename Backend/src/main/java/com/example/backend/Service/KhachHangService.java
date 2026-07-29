package com.example.backend.Service;

import com.example.backend.Entity.KhachHang;
import com.example.backend.Request.KhachHangRequest;
import com.example.backend.Response.KhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface KhachHangService {
    Page<KhachHangResponse> phanTrangKhachHang(Integer pageNo, Integer pageSize);

    void addKhachHang(KhachHangRequest khachHangRequest, MultipartFile file);

    void updateKhachHang(Integer id, KhachHangRequest khachHangRequest, MultipartFile file);

    List<KhachHangResponse> searchFullKhachHang(String keyword);

    KhachHangResponse detailKhachHang(Integer id);

    ByteArrayInputStream khachHangExcel(String keyword, Boolean gender, Integer status);

    List<KhachHang> getKhachHang();

    void addKH(KhachHangRequest khachHangRequest);

    KhachHang addQuick(KhachHangRequest request);

}

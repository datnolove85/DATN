package com.example.backend.Service;

import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Response.SanPhamChiTietResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SanPhamChiTietService {
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    public List<SanPhamChiTietResponse> getAllClient() {

        List<SanPhamChiTiet> products = sanPhamChiTietRepository.getAllClient();
        List<Object[]> images = sanPhamChiTietRepository.getAllImages();

        Map<Integer, List<String>> imageMap = new HashMap<>();

        for (Object[] obj : images) {
            Integer id = (Integer) obj[0];
            String link = (String) obj[1];

            imageMap.computeIfAbsent(id, k -> new ArrayList<>()).add(link);
        }

        return products.stream().map(p ->
                new SanPhamChiTietResponse(
                        p.getId(),
                        p.getMaSanPhamChiTiet(),
                        p.getIdSanPham().getTenSanPham(),
                        p.getIdMauSac().getTenMauSac(),
                        p.getIdKichThuoc().getTenKichThuoc(),
                        p.getGiaBan(),
                        p.getSoLuongTon(),
                        imageMap.getOrDefault(p.getId(), new ArrayList<>())
                )
        ).toList();
    }
    public SanPhamChiTietResponse getDetail(Integer id) {

        SanPhamChiTiet spct = sanPhamChiTietRepository.getDetailEntity(id);
        List<String> images = sanPhamChiTietRepository.getAllImages(id);

        SanPhamChiTietResponse dto = new SanPhamChiTietResponse(
                spct.getId(),
                spct.getMaSanPhamChiTiet(),
                spct.getIdSanPham().getTenSanPham(),
                spct.getIdMauSac().getTenMauSac(),
                spct.getIdKichThuoc().getTenKichThuoc(),
                spct.getGiaBan(),
                spct.getSoLuongTon(),
                images // 👈 thêm list ảnh
        );

        return dto;
    }


}

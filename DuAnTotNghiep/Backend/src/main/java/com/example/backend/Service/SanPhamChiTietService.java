package com.example.backend.Service;

import com.example.backend.Entity.SanPham;
import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Repository.SanPhamRepository;
import com.example.backend.Response.SanPhamChiTietResponse;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.Response.VariantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamChiTietService {

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    SanPhamRepository sanPhamRepository;

    public List<SanPhamResponse> getAllSanPham() {

        List<SanPham> sanPhams = sanPhamRepository.findAll();

        return sanPhams.stream().map(sp -> {

            // Lấy 1 SPCT đầu tiên của sản phẩm
            SanPhamChiTiet spct =
                    sanPhamChiTietRepository.findFirstByIdSanPham_Id(sp.getId());

            String image = null;
            BigDecimal giaBan = BigDecimal.ZERO;

            if (spct != null) {

                giaBan = spct.getGiaBan();

                List<String> images =
                        sanPhamChiTietRepository.getAllImages(spct.getId());

                if (!images.isEmpty()) {
                    image = images.get(0);
                }
            }

            return new SanPhamResponse(
                    sp.getId(),
                    sp.getTenSanPham(),

                    sp.getIdDanhMuc().getId(),
                    sp.getIdDanhMuc().getTenDanhMuc(),

                    sp.getIdThuongHieu().getTenThuongHieu(),
                    sp.getIdChatLieu().getTenChatLieu(),

                    giaBan,
                    image
            );

        }).collect(Collectors.toList());
    }
    // ================= GET LIST =================
    public List<SanPhamChiTietResponse> getAllClient() {

        List<SanPhamChiTiet> products =
                sanPhamChiTietRepository.getAllClient();

        List<Object[]> images =
                sanPhamChiTietRepository.getAllImages();

        Map<Integer, List<String>> imageMap = new HashMap<>();

        for (Object[] obj : images) {

            Integer id = (Integer) obj[0];
            String link = (String) obj[1];

            imageMap.computeIfAbsent(id, k -> new ArrayList<>())
                    .add(link);
        }

        return products.stream()
                .map(p -> {

                    SanPhamChiTietResponse response =
                            new SanPhamChiTietResponse(
                                    p.getId(),
                                    p.getMaSanPhamChiTiet(),
                                    p.getIdSanPham().getTenSanPham(),

                                    p.getIdSanPham().getIdDanhMuc().getId(),
                                    p.getIdSanPham().getIdDanhMuc().getTenDanhMuc(),

                                    p.getIdMauSac().getTenMauSac(),
                                    p.getIdKichThuoc().getTenKichThuoc(),

                                    p.getIdSanPham().getIdChatLieu().getTenChatLieu(),
                                    p.getIdSanPham().getIdThuongHieu().getTenThuongHieu(),

                                    p.getGiaBan(),
                                    p.getSoLuongTon(),

                                    imageMap.getOrDefault(p.getId(), new ArrayList<>()),

                                    new ArrayList<>()
                    );

                    response.setVariants(new ArrayList<>());

                    return response;
                })
                .collect(Collectors.toList());
    }

    // ================= GET DETAIL =================
    public SanPhamChiTietResponse getDetail(Integer idSanPham) {

        // 1. Lấy tất cả SPCT theo ID sản phẩm (QUAN TRỌNG NHẤT)
        List<SanPhamChiTiet> variants =
                sanPhamChiTietRepository.findVariantsByProduct(idSanPham);

        // ❌ không có sản phẩm
        if (variants == null) variants = new ArrayList<>();

        // 2. Lấy 1 biến thể đại diện
        SanPhamChiTiet spct = variants.get(0);

        // 3. Ảnh
        List<String> images =
                sanPhamChiTietRepository.getAllImages(spct.getId());

        // 4. biến thể
        List<VariantResponse> variantResponses =
                variants.stream()
                        .map(v -> new VariantResponse(
                                v.getId(),
                                v.getIdMauSac().getTenMauSac(),
                                v.getIdKichThuoc().getTenKichThuoc(),
                                v.getSoLuongTon()
                        ))
                        .collect(Collectors.toList());

        // 5. return response
        return new SanPhamChiTietResponse(
                spct.getId(),
                spct.getMaSanPhamChiTiet(),
                spct.getIdSanPham().getTenSanPham(),

                spct.getIdSanPham().getIdDanhMuc().getId(),
                spct.getIdSanPham().getIdDanhMuc().getTenDanhMuc(),

                spct.getIdMauSac().getTenMauSac(),
                spct.getIdKichThuoc().getTenKichThuoc(),

                spct.getIdSanPham().getIdChatLieu().getTenChatLieu(),
                spct.getIdSanPham().getIdThuongHieu().getTenThuongHieu(),

                spct.getGiaBan(),
                spct.getSoLuongTon(),

                images,
                variantResponses
        );
    }
}
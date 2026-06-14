package com.example.backend.Service;

import com.example.backend.Entity.KichThuoc;
import com.example.backend.Entity.MauSac;
import com.example.backend.Entity.SanPham;
import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Repository.KichThuocRepository;
import com.example.backend.Repository.MauSacRepository;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Repository.SanPhamRepository;
import com.example.backend.Request.SanPhamChiTietRequest;
import com.example.backend.Request.SanPhamCreateVariantRequest;
import com.example.backend.Response.SanPhamChiTietResponse;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.Response.VariantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamChiTietService {

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private KichThuocRepository kichThuocRepository;

    // ================= GET ALL PRODUCT =================
    public List<SanPhamResponse> getAllSanPham() {

        // 1. list product
        List<SanPham> sanPhams = sanPhamRepository.findAll();

        // 2. SPCT đại diện (theo repo mới)
        List<SanPhamChiTiet> representativeSpct =
                sanPhamChiTietRepository.findRepresentativeSpct();

        Map<Integer, SanPhamChiTiet> spctMap = new HashMap<>();

        for (SanPhamChiTiet spct : representativeSpct) {
            spctMap.put(
                    spct.getIdSanPham().getId(),
                    spct
            );
        }

        // 3. thumbnail images (theo repo mới)
        List<Object[]> thumbs =
                sanPhamChiTietRepository.getThumbnailImages();

        Map<Integer, String> imageMap = new HashMap<>();

        for (Object[] obj : thumbs) {
            Integer spctId = (Integer) obj[0];
            String link = (String) obj[1];

            imageMap.put(spctId, link);
        }

        // 4. build response
        return sanPhams.stream().map(sp -> {

            SanPhamChiTiet spct = spctMap.get(sp.getId());

            String image = null;

            if (spct != null) {
                image = imageMap.get(spct.getId());
            }

            return new SanPhamResponse(
                    sp.getId(),
                    sp.getIdDanhMuc(),
                    sp.getIdThuongHieu(),
                    sp.getIdChatLieu(),
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getMoTa(),
                    sp.getNgayTao(),
                    sp.getNgayCapNhat(),
                    sp.getTrangThai(),
                    image
            );

        }).toList();
    }


    public List<SanPhamChiTietResponse> getAllSpct() {

        List<SanPhamChiTiet> spcts =
                sanPhamChiTietRepository.findAll();

        List<String> imagesFlat =
                sanPhamChiTietRepository.getAllImagesFlat();

        Map<Integer, List<String>> imageMap = new HashMap<>();

        for (Object[] obj : sanPhamChiTietRepository.getAllImages()) {
            Integer id = (Integer) obj[0];
            String link = (String) obj[1];

            imageMap.computeIfAbsent(id, k -> new ArrayList<>())
                    .add(link);
        }

        return spcts.stream().map(spct -> {

            SanPhamChiTietResponse res = new SanPhamChiTietResponse();

            res.setId(spct.getId());

            // sản phẩm
            res.setIdSanPham(spct.getIdSanPham().getId());
            res.setTenSanPham(
                    spct.getIdSanPham().getTenSanPham()
            );

            // danh mục
            res.setTenDanhMuc(
                    spct.getIdSanPham()
                            .getIdDanhMuc()
                            .getTenDanhMuc()
            );

            // thương hiệu
            res.setTenThuongHieu(
                    spct.getIdSanPham()
                            .getIdThuongHieu()
                            .getTenThuongHieu()
            );

            // chất liệu
            res.setTenChatLieu(
                    spct.getIdSanPham()
                            .getIdChatLieu()
                            .getTenChatLieu()
            );

            // màu sắc
            res.setIdMauSac(spct.getIdMauSac().getId());
            res.setTenMauSac(
                    spct.getIdMauSac().getTenMauSac()
            );

            // kích thước
            res.setIdKichThuoc(spct.getIdKichThuoc().getId());
            res.setTenKichThuoc(
                    spct.getIdKichThuoc().getTenKichThuoc()
            );

            // thông tin SPCT
            res.setMaSanPhamChiTiet(
                    spct.getMaSanPhamChiTiet()
            );

            res.setTenSanPhamChiTiet(
                    spct.getTenSanPhamChiTiet()
            );

            res.setGiaNhap(spct.getGiaNhap());

            res.setGiaBan(spct.getGiaBan());

            res.setSoLuongTon(
                    spct.getSoLuongTon()
            );

            res.setTrangThai(
                    spct.getTrangThai()
            );

            // ảnh
            res.setImages(
                    imageMap.getOrDefault(
                            spct.getId(),
                            new ArrayList<>()
                    )
            );

            return res;

        }).toList();
    }

    public SanPhamChiTietResponse add(SanPhamChiTietRequest request) {
        System.out.println("DEBUG: Dữ liệu nhận được - ID SP: " + request.getIdSanPham() + ", Tên: " + request.getTenSanPhamChiTiet());

        SanPham sanPham = sanPhamRepository.findById(request.getIdSanPham())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        MauSac mauSac = mauSacRepository.findById(request.getIdMauSac())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc"));

        KichThuoc kichThuoc = kichThuocRepository.findById(request.getIdKichThuoc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước"));

        SanPhamChiTiet spct = new SanPhamChiTiet();

        spct.setIdSanPham(sanPham);
        spct.setIdMauSac(mauSac);
        spct.setIdKichThuoc(kichThuoc);

        spct.setMaSanPhamChiTiet(request.getMaSanPhamChiTiet());
        spct.setTenSanPhamChiTiet(request.getTenSanPhamChiTiet());

        spct.setGiaNhap(request.getGiaNhap());

        spct.setGiaBan(request.getGiaBan());

        spct.setSoLuongTon(request.getSoLuongTon());
        spct.setTrangThai(request.getTrangThai());


        SanPhamChiTiet saved =
                sanPhamChiTietRepository.save(spct);

        return mapToResponse(saved);
    }

    public SanPhamChiTietResponse update(
            Integer id,
            SanPhamChiTietRequest request) {

        SanPhamChiTiet spct =
                sanPhamChiTietRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy SPCT"));

        SanPham sanPham = sanPhamRepository.findById(request.getIdSanPham())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        MauSac mauSac = mauSacRepository.findById(request.getIdMauSac())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc"));

        KichThuoc kichThuoc = kichThuocRepository.findById(request.getIdKichThuoc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước"));

        spct.setIdSanPham(sanPham);
        spct.setIdMauSac(mauSac);
        spct.setIdKichThuoc(kichThuoc);

        spct.setMaSanPhamChiTiet(request.getMaSanPhamChiTiet());
        spct.setTenSanPhamChiTiet(request.getTenSanPhamChiTiet());

        spct.setGiaNhap(request.getGiaNhap());

        spct.setGiaBan(request.getGiaBan());

        spct.setSoLuongTon(request.getSoLuongTon());
        spct.setTrangThai(request.getTrangThai());

        SanPhamChiTiet updated =
                sanPhamChiTietRepository.save(spct);

        return mapToResponse(updated);
    }

    public SanPhamChiTietResponse getById(Integer id) {

        SanPhamChiTiet spct =
                sanPhamChiTietRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy SPCT"));

        return mapToResponse(spct);
    }

    public void delete(Integer id) {

        SanPhamChiTiet spct =
                sanPhamChiTietRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy SPCT"));

        sanPhamChiTietRepository.delete(spct);

        // hoặc soft delete
        // spct.setTrangThai(false);
        // sanPhamChiTietRepository.save(spct);
    }

    private SanPhamChiTietResponse mapToResponse(SanPhamChiTiet spct) {

        SanPhamChiTietResponse res =
                new SanPhamChiTietResponse();

        res.setId(spct.getId());

        res.setIdSanPham(spct.getIdSanPham().getId());
        res.setTenSanPham(
                spct.getIdSanPham().getTenSanPham()
        );

        res.setTenDanhMuc(
                spct.getIdSanPham()
                        .getIdDanhMuc()
                        .getTenDanhMuc()
        );

        res.setTenThuongHieu(
                spct.getIdSanPham()
                        .getIdThuongHieu()
                        .getTenThuongHieu()
        );

        res.setTenChatLieu(
                spct.getIdSanPham()
                        .getIdChatLieu()
                        .getTenChatLieu()
        );

        res.setIdMauSac(spct.getIdMauSac().getId());
        res.setTenMauSac(
                spct.getIdMauSac().getTenMauSac()
        );

        res.setIdKichThuoc(spct.getIdKichThuoc().getId());
        res.setTenKichThuoc(
                spct.getIdKichThuoc().getTenKichThuoc()
        );

        res.setMaSanPhamChiTiet(
                spct.getMaSanPhamChiTiet()
        );

        res.setTenSanPhamChiTiet(
                spct.getTenSanPhamChiTiet()
        );

        res.setGiaNhap(spct.getGiaNhap());
        res.setGiaBan(spct.getGiaBan());

        res.setSoLuongTon(spct.getSoLuongTon());
        res.setTrangThai(spct.getTrangThai());

        return res;
    }

    public List<SanPhamChiTietResponse> getByIdSP(Integer idSanPham) {

        List<SanPhamChiTiet> list =
                sanPhamChiTietRepository.findVariantsByProduct(idSanPham);

        Map<Integer, List<String>> imageMap = new HashMap<>();

        for (Object[] obj : sanPhamChiTietRepository.getAllImages()) {

            Integer spctId = (Integer) obj[0];
            String link = (String) obj[1];

            imageMap.computeIfAbsent(
                    spctId,
                    k -> new ArrayList<>()
            ).add(link);
        }

        return list.stream().map(spct -> {

            SanPhamChiTietResponse res =
                    new SanPhamChiTietResponse();

            res.setId(spct.getId());

            res.setIdSanPham(spct.getIdSanPham().getId());
            res.setTenSanPham(spct.getIdSanPham().getTenSanPham());

            res.setTenDanhMuc(
                    spct.getIdSanPham()
                            .getIdDanhMuc()
                            .getTenDanhMuc()
            );

            res.setTenThuongHieu(
                    spct.getIdSanPham()
                            .getIdThuongHieu()
                            .getTenThuongHieu()
            );

            res.setTenChatLieu(
                    spct.getIdSanPham()
                            .getIdChatLieu()
                            .getTenChatLieu()
            );

            res.setIdMauSac(spct.getIdMauSac().getId());
            res.setTenMauSac(spct.getIdMauSac().getTenMauSac());

            res.setIdKichThuoc(spct.getIdKichThuoc().getId());
            res.setTenKichThuoc(spct.getIdKichThuoc().getTenKichThuoc());

            res.setMaSanPhamChiTiet(spct.getMaSanPhamChiTiet());
            res.setTenSanPhamChiTiet(spct.getTenSanPhamChiTiet());

            res.setGiaNhap(spct.getGiaNhap());
            res.setGiaBan(spct.getGiaBan());

            res.setSoLuongTon(spct.getSoLuongTon());
            res.setTrangThai(spct.getTrangThai());

            res.setImages(
                    imageMap.getOrDefault(
                            spct.getId(),
                            new ArrayList<>()
                    )
            );

            return res;

        }).toList();
    }

    @Transactional
    public void createBulk(List<SanPhamCreateVariantRequest> reqList) {

        List<SanPhamChiTiet> list = new ArrayList<>();

        for (SanPhamCreateVariantRequest req : reqList) {

            SanPham sp = sanPhamRepository.findById(req.getIdSanPham())
                    .orElseThrow();

            MauSac mau = mauSacRepository.findById(req.getIdMauSac())
                    .orElseThrow();

            KichThuoc size = kichThuocRepository.findById(req.getIdKichThuoc())
                    .orElseThrow();

            // 🚨 CHECK TRÙNG (QUAN TRỌNG POS)
            boolean exists = sanPhamChiTietRepository
                    .existsByIdSanPhamAndIdMauSacAndIdKichThuoc(sp, mau, size);

            if (exists) continue;

            SanPhamChiTiet spct = new SanPhamChiTiet();

            spct.setIdSanPham(sp);
            spct.setIdMauSac(mau);
            spct.setIdKichThuoc(size);

            // 🔥 AUTO SKU POS
            String sku = generateSKU(sp.getTenSanPham(), mau.getTenMauSac(), size.getTenKichThuoc(), sp.getId());

            spct.setMaSanPhamChiTiet(sku);

            spct.setTenSanPhamChiTiet(
                    sp.getTenSanPham() + " " + mau.getTenMauSac() + " " + size.getTenKichThuoc()
            );

            spct.setGiaNhap(req.getGiaNhap());
            spct.setGiaBan(req.getGiaBan());
            spct.setSoLuongTon(req.getSoLuongTon());
            spct.setTrangThai(true);

            list.add(spct);
        }

        sanPhamChiTietRepository.saveAll(list);
    }
    private String generateSKU(String tenSP, String mau, String size, Integer id) {

        String cleanSP = tenSP.replaceAll("\\s+", "-").toUpperCase();
        String cleanMau = mau.replaceAll("\\s+", "-").toUpperCase();
        String cleanSize = size.replaceAll("\\s+", "-").toUpperCase();

        return cleanSP + "-" + cleanMau + "-" + cleanSize + "-" + id;
    }
}
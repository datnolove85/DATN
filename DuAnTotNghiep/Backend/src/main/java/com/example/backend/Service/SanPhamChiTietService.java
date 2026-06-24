package com.example.backend.Service;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Request.SanPhamChiTietRequest;
import com.example.backend.Request.SanPhamCreateVariantRequest;
import com.example.backend.Response.SanPhamChiTietResponse;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.Response.VariantResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

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

        // 4. tổng số lượng theo sản phẩm
        List<Object[]> tongSL = sanPhamChiTietRepository.tongSoLuongTheoSanPham();

        Map<Integer, Integer> soLuongMap = new HashMap<>();

        for (Object[] obj : tongSL) {
            Integer sanPhamId = (Integer) obj[0];
            Integer soLuong = ((Number) obj[1]).intValue();

            soLuongMap.put(sanPhamId, soLuong);
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
                    image,
                    soLuongMap.getOrDefault(sp.getId(), 0)
            );

        }).toList();
    }


    public List<SanPhamChiTietResponse> getAllSpct() {

        List<SanPhamChiTiet> spcts =
                sanPhamChiTietRepository.findAllDangKinhDoanh();

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
                    imageMap.getOrDefault(spct.getId(), new ArrayList<>())
                            .stream()
                            .map(link -> link.startsWith("/sanpham/")
                                    ? link
                                    : "/sanpham/" + link)
                            .toList()
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
            SanPhamChiTietRequest request,
            MultipartFile[] files) {

        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy SPCT"));

        SanPham sanPham = sanPhamRepository.findById(request.getIdSanPham())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        MauSac mauSac = mauSacRepository.findById(request.getIdMauSac())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc"));

        KichThuoc kichThuoc = kichThuocRepository.findById(request.getIdKichThuoc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước"));

        // Validate
        if (request.getGiaNhap().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Giá nhập phải lớn hơn 0");
        }

        if (request.getGiaBan().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Giá bán phải lớn hơn 0");
        }

        if (request.getSoLuongTon() < 0) {
            throw new RuntimeException("Số lượng tồn không được âm");
        }

        // Kiểm tra trùng biến thể (trừ chính nó)
        Optional<SanPhamChiTiet> existed =
                sanPhamChiTietRepository.findByIdSanPham_IdAndIdMauSac_IdAndIdKichThuoc_Id(
                        request.getIdSanPham(),
                        request.getIdMauSac(),
                        request.getIdKichThuoc());

        if (existed.isPresent() && !existed.get().getId().equals(id)) {
            throw new RuntimeException("Biến thể này đã tồn tại");
        }

        // Update
        spct.setIdSanPham(sanPham);
        spct.setIdMauSac(mauSac);
        spct.setIdKichThuoc(kichThuoc);

        spct.setMaSanPhamChiTiet(request.getMaSanPhamChiTiet());
        spct.setTenSanPhamChiTiet(request.getTenSanPhamChiTiet());

        spct.setGiaNhap(request.getGiaNhap());
        spct.setGiaBan(request.getGiaBan());

        spct.setSoLuongTon(request.getSoLuongTon());
        spct.setTrangThai(request.getTrangThai());

        // Nếu chưa dùng @PreUpdate thì thêm:
        // spct.setNgayCapNhat(LocalDateTime.now());

        SanPhamChiTiet updated = sanPhamChiTietRepository.save(spct);

        if (files != null && files.length > 0) {

            try {

                Path uploadPath = Paths.get("uploads/sanpham");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                for (MultipartFile file : files) {

                    String fileName = UUID.randomUUID()
                            + "_" + file.getOriginalFilename();

                    Files.copy(
                            file.getInputStream(),
                            uploadPath.resolve(fileName),
                            StandardCopyOption.REPLACE_EXISTING);

                    HinhAnh img = new HinhAnh();

                    img.setIdSanPhamChiTiet(updated);
                    img.setTenAnh(file.getOriginalFilename());
                    img.setLink(fileName);
                    img.setDinhDang(file.getContentType());
                    img.setLaAnhChinh(false);
                    img.setTrangThai(true);

                    hinhAnhRepository.save(img);
                }

            } catch (IOException e) {
                throw new RuntimeException("Lỗi upload ảnh", e);
            }
        }
        capNhatTrangThaiSanPham(request.getIdSanPham());

        return mapToResponse(updated);
    }

    public SanPhamChiTietResponse getById(Integer id) {

        SanPhamChiTiet spct =
                sanPhamChiTietRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy SPCT"));

        return mapToResponse(spct);
    }


    @Transactional
    public void delete(Integer id) {

        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy SPCT"));

        spct.setTrangThai(false);

        sanPhamChiTietRepository.save(spct);
        Integer idSanPham = spct.getIdSanPham().getId();
        capNhatTrangThaiSanPham(idSanPham);

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

        List<String> images =
                hinhAnhRepository
                        .findByIdSanPhamChiTiet_IdAndTrangThaiTrue(spct.getId())
                        .stream()
                        .map(HinhAnh::getLink)
                        .toList();


        res.setImages(
                hinhAnhRepository
                        .findByIdSanPhamChiTiet_IdAndTrangThaiTrue(spct.getId())
                        .stream()
                        .map(img -> {
                            String link = img.getLink();

                            if (link.startsWith("/sanpham/")) {
                                return link;
                            }

                            return "/sanpham/" + link;
                        })
                        .toList()
        );
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
                    imageMap.getOrDefault(spct.getId(), new ArrayList<>())
                            .stream()
                            .map(link -> link.startsWith("/sanpham/")
                                    ? link
                                    : "/sanpham/" + link)
                            .toList()
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


    private void capNhatTrangThaiSanPham(Integer idSanPham) {

        List<SanPhamChiTiet> dsSpct =
                sanPhamChiTietRepository.findByIdSanPham_Id(idSanPham);

        boolean conBienTheHoatDong =
                dsSpct.stream()
                        .anyMatch(spct -> spct.getTrangThai() == true);

        SanPham sp = sanPhamRepository.findById(idSanPham)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        sp.setTrangThai(conBienTheHoatDong ? true : false);

        sanPhamRepository.save(sp);
    }

    public String getThongKeSPCT(Integer idSanPham) {
        Integer dangKD = sanPhamChiTietRepository.countDangKinhDoanh(idSanPham);
        Integer tong = sanPhamChiTietRepository.countTong(idSanPham);

        return dangKD + "/" + tong;
    }
}
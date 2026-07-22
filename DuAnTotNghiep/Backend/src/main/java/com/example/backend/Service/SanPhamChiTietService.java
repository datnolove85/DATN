package com.example.backend.Service;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Request.SanPhamChiTietRequest;
import com.example.backend.Request.SanPhamCreateVariantRequest;
import com.example.backend.Response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.*;
import java.util.function.Function;
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

    @Autowired
    SanPhamGiamGiaRepository sanPhamGiamGiaRepository;

    // ================= GET ALL PRODUCT =================
    public List<SanPhamResponse> getAllSanPham() {

        // 1. list product
        List<SanPham> sanPhams = sanPhamRepository.findAll();

        // 2. SPCT đại diện (theo repo mới)
        List<SanPhamChiTiet> representativeSpct =
                sanPhamChiTietRepository.findRepresentativeSpctDangKinhDoanh();


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

            String image = spct == null
                    ? null
                    : imageMap.get(spct.getId());

            image = image == null ? null : "/sanpham/" + image;

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

        Map<Integer, String> imageMap = new HashMap<>();
        List<SanPhamGiamGia> dsGG = sanPhamGiamGiaRepository.findAllDangGiamGia();
        Map<Integer, DotGiamGia> giamGiaMap =
                sanPhamGiamGiaRepository.findAllDangGiamGia()
                        .stream()
                        .collect(Collectors.toMap(
                                x -> x.getSanPhamChiTiet().getId(),
                                x -> x.getDotGiamGia()
                        ));

        for (Object[] obj : sanPhamChiTietRepository.getAllImages()) {
            Integer id = (Integer) obj[0];
            String link = (String) obj[1];

            imageMap.put(id, link);
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

            DotGiamGia dot = giamGiaMap.get(spct.getId());

            if (dot != null) {

                if ("phan_tram".equals(dot.getLoaiGiamGia())) {

                    BigDecimal giam = spct.getGiaBan()
                            .multiply(dot.getGiaTriGiam())
                            .divide(BigDecimal.valueOf(100));

                    if (dot.getGiaTriGiamToiDa() != null &&
                            giam.compareTo(dot.getGiaTriGiamToiDa()) > 0) {

                        giam = dot.getGiaTriGiamToiDa();
                    }

                    res.setGiaSauGiam(spct.getGiaBan().subtract(giam));
                    res.setPhanTramGiam(dot.getGiaTriGiam().intValue());

                } else {

                    BigDecimal giam = dot.getGiaTriGiam();

                    if (giam.compareTo(spct.getGiaBan()) > 0) {
                        giam = spct.getGiaBan();
                    }

                    res.setGiaSauGiam(spct.getGiaBan().subtract(giam));
                }

                res.setDangGiamGia(true);


            } else {

                res.setGiaSauGiam(spct.getGiaBan());

                res.setDangGiamGia(false);

            }
            res.setSoLuongTon(
                    spct.getSoLuongTon()
            );

            res.setTrangThai(
                    spct.getTrangThai()
            );

            // ảnh
            String image = imageMap.get(spct.getId());

            res.setImages(
                    image == null
                            ? List.of()
                            : List.of(image.startsWith("/sanpham/")
                            ? image
                            : "/sanpham/" + image)
            );

            return res;

        }).toList();
    }

    public SanPhamChiTietResponse add(SanPhamChiTietRequest request) {

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

        spct.setGiaNhap(request.getGiaNhap());
        spct.setGiaBan(request.getGiaBan());

        spct.setSoLuongTon(request.getSoLuongTon());
        spct.setTrangThai(request.getTrangThai());


        spct.setTenSanPhamChiTiet(
                spct.getIdSanPham().getTenSanPham()
                        + " - "
                        + mauSac.getTenMauSac()
                        + " - "
                        + kichThuoc.getTenKichThuoc()
        );

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

        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy SPCT"));

        SanPhamChiTietResponse res = mapToResponse(spct);

        // Lấy danh sách giảm giá đang áp dụng
        List<SanPhamGiamGia> dsGG = sanPhamGiamGiaRepository.findAllDangGiamGia();

        DotGiamGia dot = dsGG.stream()
                .filter(x -> x.getSanPhamChiTiet().getId().equals(spct.getId()))
                .map(SanPhamGiamGia::getDotGiamGia)
                .findFirst()
                .orElse(null);

        if (dot != null) {

            if ("phan_tram".equals(dot.getLoaiGiamGia())) {

                BigDecimal giam = spct.getGiaBan()
                        .multiply(dot.getGiaTriGiam())
                        .divide(BigDecimal.valueOf(100));

                if (dot.getGiaTriGiamToiDa() != null
                        && giam.compareTo(dot.getGiaTriGiamToiDa()) > 0) {

                    giam = dot.getGiaTriGiamToiDa();
                }

                res.setGiaSauGiam(spct.getGiaBan().subtract(giam));
                res.setPhanTramGiam(dot.getGiaTriGiam().intValue());

            } else {

                BigDecimal giam = dot.getGiaTriGiam();

                if (giam.compareTo(spct.getGiaBan()) > 0) {
                    giam = spct.getGiaBan();
                }

                res.setGiaSauGiam(spct.getGiaBan().subtract(giam));
            }

            res.setDangGiamGia(true);

        } else {

            res.setGiaSauGiam(spct.getGiaBan());
            res.setDangGiamGia(false);
        }

        return res;
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
    public List<BulkVariantResponse> createBulk(List<SanPhamCreateVariantRequest> reqList) {

        List<SanPhamChiTiet> list = new ArrayList<>();

        for (SanPhamCreateVariantRequest req : reqList) {

            SanPham sp = sanPhamRepository.findById(req.getIdSanPham())
                    .orElseThrow();

            MauSac mau = mauSacRepository.findById(req.getIdMauSac())
                    .orElseThrow();

            KichThuoc size = kichThuocRepository.findById(req.getIdKichThuoc())
                    .orElseThrow();

            if (sanPhamChiTietRepository.existsByIdSanPhamAndIdMauSacAndIdKichThuoc(sp, mau, size))
                continue;

            SanPhamChiTiet spct = new SanPhamChiTiet();

            spct.setIdSanPham(sp);
            spct.setIdMauSac(mau);
            spct.setIdKichThuoc(size);

            spct.setMaSanPhamChiTiet(
                    generateSKU(
                            sp.getTenSanPham(),
                            mau.getTenMauSac(),
                            size.getTenKichThuoc(),
                            sp.getId()));

            spct.setTenSanPhamChiTiet(
                    sp.getTenSanPham()
                            + " "
                            + mau.getTenMauSac()
                            + " "
                            + size.getTenKichThuoc());

            spct.setGiaNhap(req.getGiaNhap());
            spct.setGiaBan(req.getGiaBan());
            spct.setSoLuongTon(req.getSoLuongTon());
            spct.setTrangThai(true);

            list.add(spct);
        }

        List<SanPhamChiTiet> saved = sanPhamChiTietRepository.saveAll(list);

        List<BulkVariantResponse> result = new ArrayList<>();

        for (SanPhamChiTiet spct : saved) {

            BulkVariantResponse r = new BulkVariantResponse();

            r.setId(spct.getId());
            r.setColorId(spct.getIdMauSac().getId());
            r.setSizeId(spct.getIdKichThuoc().getId());

            result.add(r);
        }

        return result;
    }
    private String removeAccent(String text) {
        if (text == null) return "";

        String result = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        // Normalizer không xử lý Đ/đ
        result = result.replace('Đ', 'D')
                .replace('đ', 'd');

        return result;
    }

    private String generateSKU(String tenSP, String mau, String size, Integer id) {

        String cleanSP = removeAccent(tenSP)
                .replaceAll("[^A-Za-z0-9]+", "-")
                .replaceAll("^-|-$", "")
                .toUpperCase();

        String cleanMau = removeAccent(mau)
                .replaceAll("[^A-Za-z0-9]+", "-")
                .replaceAll("^-|-$", "")
                .toUpperCase();

        String cleanSize = removeAccent(size)
                .replaceAll("[^A-Za-z0-9]+", "-")
                .replaceAll("^-|-$", "")
                .toUpperCase();

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

    public List<SanPhamChiTietResponse> getByIdSPOnline(Integer idSanPham) {

        List<SanPhamChiTiet> list =
                sanPhamChiTietRepository.findVariantsByProduct(idSanPham);

        Map<Integer, DotGiamGia> giamGiaMap =
                sanPhamGiamGiaRepository.findAllDangGiamGia()
                        .stream()
                        .collect(Collectors.toMap(
                                x -> x.getSanPhamChiTiet().getId(),
                                x -> x.getDotGiamGia()
                        ));
        Map<Integer, List<String>> imageMap = new HashMap<>();

        for (Object[] obj : sanPhamChiTietRepository.getAllImagesForVariant()) {

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
            DotGiamGia dot = giamGiaMap.get(spct.getId());

            if (dot != null) {

                if ("phan_tram".equals(dot.getLoaiGiamGia())) {

                    BigDecimal giam = spct.getGiaBan()
                            .multiply(dot.getGiaTriGiam())
                            .divide(BigDecimal.valueOf(100));

                    if (dot.getGiaTriGiamToiDa() != null &&
                            giam.compareTo(dot.getGiaTriGiamToiDa()) > 0) {

                        giam = dot.getGiaTriGiamToiDa();
                    }

                    res.setGiaSauGiam(
                            spct.getGiaBan().subtract(giam)
                    );

                    res.setPhanTramGiam(
                            dot.getGiaTriGiam().intValue()
                    );

                } else {

                    BigDecimal giam = dot.getGiaTriGiam();

                    if (giam.compareTo(spct.getGiaBan()) > 0) {
                        giam = spct.getGiaBan();
                    }

                    res.setGiaSauGiam(
                            spct.getGiaBan().subtract(giam)
                    );

                    // Nếu giảm theo tiền thì không có %
                    res.setPhanTramGiam(null);
                }

                res.setDangGiamGia(true);

            } else {

                res.setGiaSauGiam(spct.getGiaBan());

                res.setDangGiamGia(false);

                res.setPhanTramGiam(null);
            }

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

    public ProductVariantResponse getVariantForShop(Integer productId) {

        List<SanPhamChiTietResponse> variants = getByIdSPOnline(productId);

        if (variants.isEmpty()) {
            throw new RuntimeException("Không tìm thấy sản phẩm");
        }

        Map<Integer, ColorShopResponse> colorMap = new LinkedHashMap<>();

        for (SanPhamChiTietResponse variant : variants) {

            ColorShopResponse color = colorMap.computeIfAbsent(
                    variant.getIdMauSac(),
                    id -> new ColorShopResponse(
                            id,
                            variant.getTenMauSac(),
                            new ArrayList<>()
                    )
            );

            color.getVariants().add(variant);
        }

        List<Object[]> galleryData =
                sanPhamChiTietRepository.getGalleryByProduct(productId);

        Set<String> existed = new HashSet<>();

        List<GalleryImageResponse> gallery =
                new ArrayList<>();

        for (Object[] obj : galleryData) {

            Integer spctId = (Integer) obj[0];
            Integer mauSacId = (Integer) obj[1];
            Integer kichThuocId = (Integer) obj[2];
            String link = (String) obj[3];

            // loại ảnh trùng
            if (existed.add(link)) {

                gallery.add(

                        new GalleryImageResponse(

                                spctId,

                                mauSacId,

                                kichThuocId,

                                link.startsWith("/sanpham/")
                                        ? link
                                        : "/sanpham/" + link

                        )

                );

            }

        }

        return new ProductVariantResponse(

                variants.get(0).getIdSanPham(),

                variants.get(0).getTenSanPham(),

                new ArrayList<>(colorMap.values()),

                gallery

        );

    }

}
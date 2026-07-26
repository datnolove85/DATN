package com.example.backend.Service;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Request.SanPhamChiTietRequest;
import com.example.backend.Request.SanPhamCreateVariantRequest;
import com.example.backend.Response.*;
import com.example.backend.websocket.PosEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private PosSocketService posSocketService;


    //Map
    private SanPhamChiTietResponse mapToResponse(SanPhamChiTiet spct) {

        SanPhamChiTietResponse res = new SanPhamChiTietResponse();

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

        // === 1. BỔ SUNG KHẢ DỤNG & TẠM GIỮ ===
        int ton = spct.getSoLuongTon() != null ? spct.getSoLuongTon() : 0;
        int tamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;

        res.setSoLuongTon(ton);
        res.setSoLuongTamGiu(tamGiu); // Gán nếu DTO có trường này
        res.setSoLuongKhaDung(Math.max(0, ton - tamGiu)); // Tính số lượng thực tế có thể bán

        res.setTrangThai(spct.getTrangThai());

        // === 2. TỐI ƯU LOAD HÌNH ẢNH (Chỉ gọi DB 1 lần) ===
        List<String> images = hinhAnhRepository
                .findByIdSanPhamChiTiet_IdAndTrangThaiTrue(spct.getId())
                .stream()
                .map(img -> {
                    String link = img.getLink();
                    if (link != null && link.startsWith("/sanpham/")) {
                        return link;
                    }
                    return "/sanpham/" + link;
                })
                .toList();

        res.setImages(images);

        return res;
    }


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
    //Get All Spct
    public List<SanPhamChiTietResponse> getAllSpct() {

        List<SanPhamChiTiet> spcts =
                sanPhamChiTietRepository.findAllDangKinhDoanh();

        Map<Integer, String> imageMap = new HashMap<>();

        // 1. Tối ưu: Chỉ gọi DB 1 lần & xử lý chống crash khi trùng key
        Map<Integer, DotGiamGia> giamGiaMap =
                sanPhamGiamGiaRepository.findAllDangGiamGia()
                        .stream()
                        .collect(Collectors.toMap(
                                x -> x.getSanPhamChiTiet().getId(),
                                SanPhamGiamGia::getDotGiamGia,
                                (existing, replacement) -> existing // Nếu trùng thì lấy cái đầu tiên
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

            // Tính giá giảm
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

            // === 2. BỔ SUNG SỐ LƯỢNG TẠM GIỮ & KHẢ DỤNG ===
            int ton = spct.getSoLuongTon() != null ? spct.getSoLuongTon() : 0;
            int tamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;

            res.setSoLuongTon(ton);
            res.setSoLuongTamGiu(tamGiu);
            res.setSoLuongKhaDung(Math.max(0, ton - tamGiu));

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

        // 1. Set số lượng tồn bằng số lượng đầu vào
        spct.setSoLuongTon(request.getSoLuongTon());

        // 2. Mới thêm thì tạm giữ = 0 -> Khả dụng sẽ tự động = Số lượng tồn
        spct.setSoLuongTamGiu(0);

        spct.setTrangThai(request.getTrangThai());

        SanPhamChiTiet saved = sanPhamChiTietRepository.save(spct);

        return mapToResponse(saved);
    }

    @Transactional // 🔴 Giúp thao tác xóa/sửa HDCT và SPCT diễn ra đồng bộ trong 1 Transaction
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

        // Kiểm tra trùng biến thể (trừ chính nó)
        Optional<SanPhamChiTiet> existed =
                sanPhamChiTietRepository.findByIdSanPham_IdAndIdMauSac_IdAndIdKichThuoc_Id(
                        request.getIdSanPham(),
                        request.getIdMauSac(),
                        request.getIdKichThuoc());

        if (existed.isPresent() && !existed.get().getId().equals(id)) {
            throw new RuntimeException("Biến thể này đã tồn tại");
        }

        // Update thông tin cơ bản
        spct.setIdSanPham(sanPham);
        spct.setIdMauSac(mauSac);
        spct.setIdKichThuoc(kichThuoc);

        spct.setGiaNhap(request.getGiaNhap());
        spct.setGiaBan(request.getGiaBan());
        spct.setTrangThai(request.getTrangThai());

        if (spct.getSoLuongTamGiu() == null) {
            spct.setSoLuongTamGiu(0);
        }

        // ================== XỬ LÝ TỒN KHO & ĐIỀU CHỈNH HÓA ĐƠN CHỜ ==================
        Integer soLuongTonMoi = request.getSoLuongTon() != null ? request.getSoLuongTon() : 0;
        Integer tamGiuHienTai = spct.getSoLuongTamGiu();

        // Trường hợp Admin hạ Tồn kho nhỏ hơn số lượng đang Tạm giữ
        if (soLuongTonMoi < tamGiuHienTai) {
            int soLuongCanXen = tamGiuHienTai - soLuongTonMoi;

            // Lấy danh sách HDCT của các hóa đơn "cho_xac_nhan" chứa SPCT này
            List<HoaDonChiTiet> dsHdctCho = hoaDonChiTietRepository
                    .findByIdSanPhamChiTiet_IdAndIdHoaDon_TrangThai(spct.getId(), "cho_xac_nhan");

            for (HoaDonChiTiet ct : dsHdctCho) {
                if (soLuongCanXen <= 0) break;

                int soLuongMua = ct.getSoLuong() != null ? ct.getSoLuong() : 0;

                if (soLuongMua <= soLuongCanXen) {
                    // Xóa hẳn dòng sản phẩm này khỏi hóa đơn chờ
                    soLuongCanXen -= soLuongMua;
                    hoaDonChiTietRepository.delete(ct);
                } else {
                    // Giảm bớt số lượng mua trong hóa đơn chờ
                    int soLuongConLai = soLuongMua - soLuongCanXen;
                    ct.setSoLuong(soLuongConLai);
                    ct.setThanhTien(ct.getDonGia().multiply(BigDecimal.valueOf(soLuongConLai)));
                    SanPhamChiTiet updated = sanPhamChiTietRepository.save(spct);
                    soLuongCanXen = 0;
                }
            }

            // Ép Tạm giữ bằng đúng Tồn mới (Khả dụng = 0)
            spct.setSoLuongTamGiu(soLuongTonMoi);
        }

        // Cập nhật tồn kho mới
        spct.setSoLuongTon(soLuongTonMoi);

        spct.setTenSanPhamChiTiet(
                sanPham.getTenSanPham()
                        + " - "
                        + mauSac.getTenMauSac()
                        + " - "
                        + kichThuoc.getTenKichThuoc()
        );

        SanPhamChiTiet updated = sanPhamChiTietRepository.save(spct);

        // ================== BẮN SOCKET THÔNG BÁO TỚI POS ==================
        try {
            posSocketService.send(
                    new PosEvent("STOCK_FORCE_ADJUSTED", null, updated.getId(), updated.getSoLuongTon())
            );
        } catch (Exception e) {
            System.err.println("Lỗi gửi Socket: " + e.getMessage());
        }

        // Xử lý upload ảnh (Giữ nguyên)
        if (files != null && files.length > 0) {
            try {
                Path uploadPath = Paths.get("uploads/sanpham");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                for (MultipartFile file : files) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

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


    public List<SanPhamChiTietResponse> getByIdSP(Integer idSanPham) {

        List<SanPhamChiTiet> list =
                sanPhamChiTietRepository.findVariantsByProduct(idSanPham);

        Map<Integer, List<String>> imageMap = new HashMap<>();
        // Lấy danh sách sản phẩm đang nằm trong đợt giảm giá
        Map<Integer, DotGiamGia> giamGiaMap =
                sanPhamGiamGiaRepository.findAllDangGiamGia()
                        .stream()
                        .collect(Collectors.toMap(
                                x -> x.getSanPhamChiTiet().getId(),
                                SanPhamGiamGia::getDotGiamGia,
                                (existing, replacement) -> existing
                        ));

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
            // ===================== THÔNG TIN GIẢM GIÁ =====================
            DotGiamGia dot = giamGiaMap.get(spct.getId());

            if (dot != null) {

                if ("phan_tram".equals(dot.getLoaiGiamGia())) {

                    BigDecimal giam = spct.getGiaBan()
                            .multiply(dot.getGiaTriGiam())
                            .divide(BigDecimal.valueOf(100));

                    if (dot.getGiaTriGiamToiDa() != null
                            && giam.compareTo(dot.getGiaTriGiamToiDa()) > 0) {

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
                }

                res.setDangGiamGia(true);

            } else {

                res.setGiaSauGiam(spct.getGiaBan());

                res.setDangGiamGia(false);
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

        List<SanPhamChiTiet> list = sanPhamChiTietRepository.findVariantsByProduct(idSanPham);

        // Map đợt giảm giá
        Map<Integer, DotGiamGia> giamGiaMap = sanPhamGiamGiaRepository.findAllDangGiamGia()
                .stream()
                .collect(Collectors.toMap(
                        x -> x.getSanPhamChiTiet().getId(),
                        x -> x.getDotGiamGia(),
                        (existing, replacement) -> existing // Tránh crash nếu trùng key
                ));

        // Map hình ảnh
        Map<Integer, List<String>> imageMap = new HashMap<>();
        for (Object[] obj : sanPhamChiTietRepository.getAllImagesForVariant()) {
            Integer spctId = (Integer) obj[0];
            String link = (String) obj[1];

            imageMap.computeIfAbsent(spctId, k -> new ArrayList<>()).add(link);
        }

        return list.stream()
                // FIX 1: Lọc bỏ các biến thể đã bị ẩn/ngưng kinh doanh (trangThai = false)
                .filter(spct -> Boolean.TRUE.equals(spct.getTrangThai()))
                .map(spct -> {

                    SanPhamChiTietResponse res = new SanPhamChiTietResponse();

                    res.setId(spct.getId());
                    res.setIdSanPham(spct.getIdSanPham().getId());
                    res.setTenSanPham(spct.getIdSanPham().getTenSanPham());

                    res.setTenDanhMuc(
                            spct.getIdSanPham().getIdDanhMuc() != null
                                    ? spct.getIdSanPham().getIdDanhMuc().getTenDanhMuc() : null
                    );
                    res.setTenThuongHieu(
                            spct.getIdSanPham().getIdThuongHieu() != null
                                    ? spct.getIdSanPham().getIdThuongHieu().getTenThuongHieu() : null
                    );
                    res.setTenChatLieu(
                            spct.getIdSanPham().getIdChatLieu() != null
                                    ? spct.getIdSanPham().getIdChatLieu().getTenChatLieu() : null
                    );

                    res.setIdMauSac(spct.getIdMauSac().getId());
                    res.setTenMauSac(spct.getIdMauSac().getTenMauSac());

                    res.setIdKichThuoc(spct.getIdKichThuoc().getId());
                    res.setTenKichThuoc(spct.getIdKichThuoc().getTenKichThuoc());

                    res.setMaSanPhamChiTiet(spct.getMaSanPhamChiTiet());
                    res.setTenSanPhamChiTiet(spct.getTenSanPhamChiTiet());

                    res.setGiaNhap(spct.getGiaNhap());
                    res.setGiaBan(spct.getGiaBan());

                    // FIX 2: Bổ sung tính toán Số lượng tồn / Tạm giữ / Khả dụng
                    Integer soLuongTon = spct.getSoLuongTon() != null ? spct.getSoLuongTon() : 0;
                    // Nếu Entity SanPhamChiTiet có trường soLuongTamGiu thì lấy, không thì mặc định là 0
                    Integer soLuongTamGiu = (spct.getSoLuongTamGiu() != null) ? spct.getSoLuongTamGiu() : 0;
                    Integer soLuongKhaDung = soLuongTon - soLuongTamGiu;

                    res.setSoLuongTon(soLuongTon);
                    res.setSoLuongTamGiu(soLuongTamGiu);
                    res.setSoLuongKhaDung(Math.max(0, soLuongKhaDung));
                    res.setTrangThai(spct.getTrangThai());

                    // FIX 3: Xử lý giảm giá & tính phần trăm
                    DotGiamGia dot = giamGiaMap.get(spct.getId());

                    if (dot != null) {
                        if ("phan_tram".equals(dot.getLoaiGiamGia())) {
                            BigDecimal giam = spct.getGiaBan()
                                    .multiply(dot.getGiaTriGiam())
                                    .divide(BigDecimal.valueOf(100));

                            if (dot.getGiaTriGiamToiDa() != null && giam.compareTo(dot.getGiaTriGiamToiDa()) > 0) {
                                giam = dot.getGiaTriGiamToiDa();
                            }

                            res.setGiaSauGiam(spct.getGiaBan().subtract(giam));
                            res.setPhanTramGiam(dot.getGiaTriGiam().intValue());

                        } else {
                            // Giảm theo số tiền cố định
                            BigDecimal giam = dot.getGiaTriGiam();
                            if (giam.compareTo(spct.getGiaBan()) > 0) {
                                giam = spct.getGiaBan();
                            }

                            res.setGiaSauGiam(spct.getGiaBan().subtract(giam));

                            // Tự quy đổi sang % giảm tương ứng cho FE dễ hiển thị
                            if (spct.getGiaBan() != null && spct.getGiaBan().compareTo(BigDecimal.ZERO) > 0) {
                                int pt = giam.multiply(BigDecimal.valueOf(100))
                                        .divide(spct.getGiaBan(), 0, RoundingMode.HALF_UP)
                                        .intValue();
                                res.setPhanTramGiam(pt);
                            } else {
                                res.setPhanTramGiam(0);
                            }
                        }
                        res.setDangGiamGia(true);

                    } else {
                        res.setGiaSauGiam(spct.getGiaBan());
                        res.setDangGiamGia(false);
                        res.setPhanTramGiam(0); // Trả về 0 thay vì null để FE đỡ phải check null
                    }

                    // Xử lý đường dẫn Ảnh
                    res.setImages(
                            imageMap.getOrDefault(spct.getId(), new ArrayList<>())
                                    .stream()
                                    .map(link -> link.startsWith("/sanpham/") ? link : "/sanpham/" + link)
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
package com.example.backend.Service;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Request.SanPhamChiTietRequest;
import com.example.backend.Request.SanPhamCreateVariantRequest;
import com.example.backend.Response.*;
import com.example.backend.websocket.PosAlertEvent;
import com.example.backend.websocket.PosEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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

    @Autowired
    HoaDonRepository hoaDonRepository;


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

        List<SanPhamChiTiet> spcts = sanPhamChiTietRepository.findAllDangKinhDoanh();

        // 1. Map hình ảnh
        Map<Integer, String> imageMap = new HashMap<>();
        for (Object[] obj : sanPhamChiTietRepository.getAllImages()) {
            imageMap.put((Integer) obj[0], (String) obj[1]);
        }

        // 2. Map Đợt giảm giá
        Map<Integer, DotGiamGia> giamGiaMap =
                sanPhamGiamGiaRepository.findAllDangGiamGia()
                        .stream()
                        .collect(Collectors.toMap(
                                x -> x.getSanPhamChiTiet().getId(),
                                SanPhamGiamGia::getDotGiamGia,
                                (existing, replacement) -> existing
                        ));

        // 3. Tối ưu: Lấy số lượng đã bán từ Hóa đơn (Chỉ gọi DB 1 lần)
        Map<Integer, Integer> soldMap = hoaDonChiTietRepository.getSoLuongDaBanMap()
                .stream()
                .collect(Collectors.toMap(
                        obj -> (Integer) obj[0],
                        obj -> obj[1] != null ? ((Number) obj[1]).intValue() : 0,
                        (existing, replacement) -> existing
                ));

        return spcts.stream().map(spct -> {

            SanPhamChiTietResponse res = new SanPhamChiTietResponse();

            res.setId(spct.getId());

            // Sản phẩm
            res.setIdSanPham(spct.getIdSanPham().getId());
            res.setTenSanPham(spct.getIdSanPham().getTenSanPham());

            // Danh mục, Thương hiệu, Chất liệu
            res.setTenDanhMuc(spct.getIdSanPham().getIdDanhMuc().getTenDanhMuc());
            res.setTenThuongHieu(spct.getIdSanPham().getIdThuongHieu().getTenThuongHieu());
            res.setTenChatLieu(spct.getIdSanPham().getIdChatLieu().getTenChatLieu());

            // Màu sắc, Kích thước
            res.setIdMauSac(spct.getIdMauSac().getId());
            res.setTenMauSac(spct.getIdMauSac().getTenMauSac());
            res.setIdKichThuoc(spct.getIdKichThuoc().getId());
            res.setTenKichThuoc(spct.getIdKichThuoc().getTenKichThuoc());

            // Thông tin cơ bản
            res.setMaSanPhamChiTiet(spct.getMaSanPhamChiTiet());
            res.setTenSanPhamChiTiet(spct.getTenSanPhamChiTiet());
            res.setGiaNhap(spct.getGiaNhap());
            res.setGiaBan(spct.getGiaBan());
            res.setMoTa(spct.getIdSanPham().getMoTa());

            // Gán Số lượng đã bán
            res.setSoLuongDaBan(soldMap.getOrDefault(spct.getId(), 0));

            // Tính giá giảm & Thời gian kết thúc
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
                res.setNgayKetThuc(dot.getNgayKetThuc()); // Gán thời gian kết thúc đợt giảm giá

            } else {

                res.setGiaSauGiam(spct.getGiaBan());
                res.setDangGiamGia(false);
                res.setNgayKetThuc(null);

            }

            // Số lượng tồn & Khả dụng
            int ton = spct.getSoLuongTon() != null ? spct.getSoLuongTon() : 0;
            int tamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;

            res.setSoLuongTon(ton);
            res.setSoLuongTamGiu(tamGiu);
            res.setSoLuongKhaDung(Math.max(0, ton - tamGiu));

            res.setTrangThai(spct.getTrangThai());

            // Hình ảnh
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


    @Transactional
    public Map<String, Object> update(
            Integer id,
            SanPhamChiTietRequest request,
            MultipartFile[] files) {

        // 1. Tìm kiếm các đối tượng liên quan
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy SPCT"));

        SanPham sanPham = sanPhamRepository.findById(request.getIdSanPham())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        MauSac mauSac = mauSacRepository.findById(request.getIdMauSac())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc"));

        KichThuoc kichThuoc = kichThuocRepository.findById(request.getIdKichThuoc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước"));

        // 2. Kiểm tra trùng biến thể (trừ chính nó)
        Optional<SanPhamChiTiet> existed =
                sanPhamChiTietRepository.findByIdSanPham_IdAndIdMauSac_IdAndIdKichThuoc_Id(
                        request.getIdSanPham(),
                        request.getIdMauSac(),
                        request.getIdKichThuoc());

        if (existed.isPresent() && !existed.get().getId().equals(id)) {
            throw new RuntimeException("Biến thể này đã tồn tại");
        }

        // Lưu lại giá bán cũ để so sánh
        BigDecimal giaBanCu = spct.getGiaBan();

        // 3. Update thông tin cơ bản
        spct.setIdSanPham(sanPham);
        spct.setIdMauSac(mauSac);
        spct.setIdKichThuoc(kichThuoc);

        spct.setGiaNhap(request.getGiaNhap());
        spct.setGiaBan(request.getGiaBan());
        spct.setTrangThai(request.getTrangThai());

        // ================== 3.1. CẬP NHẬT ĐƠN GIÁ CHO HÓA ĐƠN CHỜ TẠI QUẦY (POS) ==================
        // Đơn Online giữ nguyên giá lúc đặt. Đơn TẠI QUẦY (chờ) sẽ tự động cập nhật theo giá bán mới.
        BigDecimal giaBanMoi = request.getGiaBan();
        if (giaBanMoi != null && (giaBanCu == null || giaBanCu.compareTo(giaBanMoi) != 0)) {
            List<HoaDonChiTiet> dsHdctChoTaiQuay = hoaDonChiTietRepository
                    .findByIdSanPhamChiTiet_IdAndIdHoaDon_TrangThaiAndIdHoaDon_LoaiHoaDon(
                            spct.getId(), "cho_xac_nhan", "TAI_QUAY");

            if (!dsHdctChoTaiQuay.isEmpty()) {
                Set<HoaDon> dsHoaDonCanCapNhat = new HashSet<>();

                for (HoaDonChiTiet ct : dsHdctChoTaiQuay) {
                    ct.setDonGia(giaBanMoi);
                    int sl = ct.getSoLuong() != null ? ct.getSoLuong() : 0;
                    ct.setThanhTien(giaBanMoi.multiply(BigDecimal.valueOf(sl)));
                    hoaDonChiTietRepository.save(ct);

                    dsHoaDonCanCapNhat.add(ct.getIdHoaDon());
                }

                // Tính toán lại tổng tiền các hóa đơn POS bị ảnh hưởng
                for (HoaDon hd : dsHoaDonCanCapNhat) {
                    List<HoaDonChiTiet> ctConLai = hoaDonChiTietRepository.findByIdHoaDon_Id(hd.getId());

                    BigDecimal tongTienHangMoi = ctConLai.stream()
                            .map(HoaDonChiTiet::getThanhTien)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hd.setTongTienHang(tongTienHangMoi);
                    BigDecimal tongGiamGia = hd.getTongGiamGia() != null ? hd.getTongGiamGia() : BigDecimal.ZERO;
                    hd.setTongThanhToan(tongTienHangMoi.subtract(tongGiamGia).max(BigDecimal.ZERO));

                    hoaDonRepository.save(hd);
                }
            }
        }

        // ================== 3.5. XỬ LÝ KHI CHUYỂN SANG NGỪNG KINH DOANH ==================
        boolean isNgungKinhDoanh = Boolean.FALSE.equals(request.getTrangThai())
                || "false".equalsIgnoreCase(String.valueOf(request.getTrangThai()).trim());

        if (isNgungKinhDoanh) {
            // 🔴 CHỈ XÓA các chi tiết hóa đơn chờ THUỘC KÊNH TẠI QUẦY. Đơn online được giữ nguyên!
            List<HoaDonChiTiet> dsHdctChoTaiQuay = hoaDonChiTietRepository
                    .findByIdSanPhamChiTiet_IdAndIdHoaDon_TrangThaiAndIdHoaDon_LoaiHoaDon(
                            spct.getId(), "cho_xac_nhan", "TAI_QUAY");

            if (!dsHdctChoTaiQuay.isEmpty()) {
                Set<HoaDon> dsHoaDonCanCapNhat = new HashSet<>();

                for (HoaDonChiTiet ct : dsHdctChoTaiQuay) {
                    HoaDon hd = ct.getIdHoaDon();
                    dsHoaDonCanCapNhat.add(hd);

                    hoaDonChiTietRepository.delete(ct);
                }

                hoaDonChiTietRepository.flush();

                // Tính toán lại tổng tiền Hóa đơn tại quầy bị ảnh hưởng
                for (HoaDon hd : dsHoaDonCanCapNhat) {
                    List<HoaDonChiTiet> ctConLai = hoaDonChiTietRepository.findByIdHoaDon_Id(hd.getId());

                    BigDecimal tongTienHangMoi = ctConLai.stream()
                            .map(HoaDonChiTiet::getThanhTien)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    hd.setTongTienHang(tongTienHangMoi);
                    BigDecimal tongGiamGia = hd.getTongGiamGia() != null ? hd.getTongGiamGia() : BigDecimal.ZERO;
                    hd.setTongThanhToan(tongTienHangMoi.subtract(tongGiamGia).max(BigDecimal.ZERO));

                    hoaDonRepository.save(hd);
                }
            }
        }

        // ================== 4. XỬ LÝ TỒN KHO & XÉN ĐƠN CHỜ TẠI QUẦY ==================
        Integer soLuongTonMoi = request.getSoLuongTon() != null ? request.getSoLuongTon() : 0;

        // STEP 4.1: TÍNH SỐ LƯỢNG BẤT XÂM PHẠM (Đã xác nhận, đang giao, VÀ CẢ ĐƠN CHỜ ONLINE)
        List<String> trangThaiKhongThuongLuong = List.of(
                "da_xac_nhan",
                "cho_van_chuyen",
                "dang_giao"
        );
        Integer soLuongDaXacNhanHoacGiao = hoaDonChiTietRepository
                .sumSoLuongBySpctAndTrangThaiIn(spct.getId(), trangThaiKhongThuongLuong);
        if (soLuongDaXacNhanHoacGiao == null) soLuongDaXacNhanHoacGiao = 0;

        // Nhóm Đơn Online chờ xác nhận
        Integer soLuongChoOnline = hoaDonChiTietRepository
                .sumSoLuongBySpctAndTrangThaiAndLoaiHoaDon(spct.getId(), "cho_xac_nhan", "ONLINE");
        if (soLuongChoOnline == null) soLuongChoOnline = 0;

        // TỔNG SỐ LƯỢNG KHÔNG THỂ XÉN
        int soLuongKhongTheXen = soLuongDaXacNhanHoacGiao + soLuongChoOnline;

        // BÁO LỖI CHI TIẾT NẾU TỒN MỚI NHỎ HƠN MỨC AN TOÀN
        if (soLuongTonMoi < soLuongKhongTheXen) {
            throw new RuntimeException(String.format(
                    "Không thể giảm tồn kho về %d! Đang có %d sản phẩm bị khóa (Gồm: %d sp trong đơn ĐÃ XÁC NHẬN/ĐANG GIAO và %d sp trong ĐƠN ONLINE CHỜ XÁC NHẬN).",
                    soLuongTonMoi,
                    soLuongKhongTheXen,
                    soLuongDaXacNhanHoacGiao,
                    soLuongChoOnline
            ));
        }

        // STEP 4.2: TÍNH SỐ LƯỢNG ĐANG NẰM TRONG CÁC ĐƠN CHỜ TẠI QUẦY (Được phép xén)
        Integer soLuongChoXacNhanTaiQuay = hoaDonChiTietRepository
                .sumSoLuongBySpctAndTrangThaiAndLoaiHoaDon(spct.getId(), "cho_xac_nhan", "TAI_QUAY");
        if (soLuongChoXacNhanTaiQuay == null) soLuongChoXacNhanTaiQuay = 0;

        // TỔNG TẠM GIỮ THỰC TẾ TRONG DATABASE
        int tongSoLuongTamGiuThucTe = soLuongKhongTheXen + soLuongChoXacNhanTaiQuay;
        int soLuongBiXenInNotification = 0;

        // STEP 4.3: NẾU TỒN MỚI HẠ THẤP HƠN TỔNG SỐ LƯỢNG ĐANG TẠM GIỮ -> XÉN ĐƠN TẠI QUẦY
        if (soLuongTonMoi < tongSoLuongTamGiuThucTe) {

            int soLuongCanXen = tongSoLuongTamGiuThucTe - soLuongTonMoi;
            soLuongBiXenInNotification = soLuongCanXen;

            // 🔴 CHỈ LẤY DANH SÁCH ĐƠN CHỜ TẠI QUẦY ĐỂ XÉN (Chuẩn hóa chữ hoa "TAI_QUAY")
            List<HoaDonChiTiet> dsHdctChoTaiQuay = hoaDonChiTietRepository
                    .findByIdSanPhamChiTiet_IdAndIdHoaDon_TrangThaiAndIdHoaDon_LoaiHoaDon(
                            spct.getId(), "cho_xac_nhan", "TAI_QUAY");

            Set<HoaDon> dsHoaDonCanCapNhat = new HashSet<>();

            for (HoaDonChiTiet ct : dsHdctChoTaiQuay) {
                if (soLuongCanXen <= 0) break;

                dsHoaDonCanCapNhat.add(ct.getIdHoaDon());
                int soLuongMua = ct.getSoLuong() != null ? ct.getSoLuong() : 0;

                if (soLuongMua <= soLuongCanXen) {
                    soLuongCanXen -= soLuongMua;
                    hoaDonChiTietRepository.delete(ct);
                } else {
                    int soLuongConLai = soLuongMua - soLuongCanXen;
                    ct.setSoLuong(soLuongConLai);
                    ct.setThanhTien(ct.getDonGia().multiply(BigDecimal.valueOf(soLuongConLai)));
                    hoaDonChiTietRepository.save(ct);
                    soLuongCanXen = 0;
                }
            }

            // Cập nhật lại tổng tiền hóa đơn tại quầy bị xén
            for (HoaDon hd : dsHoaDonCanCapNhat) {
                List<HoaDonChiTiet> ctConLai = hoaDonChiTietRepository.findByIdHoaDon_Id(hd.getId());

                BigDecimal tongTienHangMoi = ctConLai.stream()
                        .map(HoaDonChiTiet::getThanhTien)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                hd.setTongTienHang(tongTienHangMoi);
                BigDecimal tongGiamGia = hd.getTongGiamGia() != null ? hd.getTongGiamGia() : BigDecimal.ZERO;
                hd.setTongThanhToan(tongTienHangMoi.subtract(tongGiamGia).max(BigDecimal.ZERO));

                hoaDonRepository.save(hd);
            }

            tongSoLuongTamGiuThucTe = soLuongTonMoi;
        }

        // ĐỒNG BỘ CẢ 2 TRƯỜNG VÀO DATABASE
        spct.setSoLuongTamGiu(tongSoLuongTamGiuThucTe);
        spct.setSoLuongTon(soLuongTonMoi);

        spct.setTenSanPhamChiTiet(
                sanPham.getTenSanPham()
                        + " - "
                        + mauSac.getTenMauSac()
                        + " - "
                        + kichThuoc.getTenKichThuoc()
        );

        SanPhamChiTiet updated = sanPhamChiTietRepository.save(spct);

        // ================== 5. TẠO CÂU THÔNG BÁO CHI TIẾT ==================
        // ================== 5. TẠO CÂU THÔNG BÁO CHI TIẾT ==================
        // Lấy tên chi tiết sản phẩm (hoặc lấy từ spct.getTenSanPhamChiTiet())
        String tenSpct = sanPham.getTenSanPham() + " - " + mauSac.getTenMauSac() + " - " + kichThuoc.getTenKichThuoc();

        final String thongBao;
        if (isNgungKinhDoanh) {
            thongBao = "Sản phẩm [" + tenSpct + "] đã chuyển sang NGỪNG KINH DOANH và tự động xóa khỏi các hóa đơn chờ tại quầy!";
        } else if (soLuongBiXenInNotification > 0) {
            thongBao = "Cập nhật thành công! Tồn kho về " + soLuongTonMoi + " (Đã tự động giảm/xóa " + soLuongBiXenInNotification + " sản phẩm ở đơn chờ tại quầy)!";
        } else {
            thongBao = "Hệ thống đã cập nhật sản phẩm!";
        }

        // ================== 6. BẮN SOCKET THÔNG BÁO TỚI POS ==================
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    posSocketService.send(
                            new PosAlertEvent("STOCK_FORCE_ADJUSTED", thongBao, updated.getId(), updated.getSoLuongTon())
                    );
                } catch (Exception e) {
                    System.err.println("Lỗi gửi Socket: " + e.getMessage());
                }
            }
        });

        // ================== 7. XỬ LÝ UPLOAD ẢNH ==================
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

        // ================== 8. ĐÓNG GÓI KẾT QUẢ TRẢ VỀ ==================
        Map<String, Object> response = new HashMap<>();
        response.put("data", mapToResponse(updated));
        response.put("message", thongBao);

        return response;
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
    public Map<String, Object> delete(Integer id) {

        // 1. Tìm kiếm sản phẩm chi tiết
        SanPhamChiTiet spct = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

        int soLuongChiTietHDBiXoa = 0;

        // ================== 1. XÓA SPCT KHỎI CÁC HÓA ĐƠN CHỜ TẠI QUẦY (TAI_QUAY) ==================
        List<HoaDonChiTiet> dsHdctChoTaiQuay = hoaDonChiTietRepository
                .findByIdSanPhamChiTiet_IdAndIdHoaDon_TrangThaiAndIdHoaDon_LoaiHoaDon(
                        spct.getId(), "cho_xac_nhan", "TAI_QUAY");

        if (!dsHdctChoTaiQuay.isEmpty()) {
            Set<HoaDon> dsHoaDonCanCapNhat = new HashSet<>();

            for (HoaDonChiTiet ct : dsHdctChoTaiQuay) {
                HoaDon hd = ct.getIdHoaDon();
                dsHoaDonCanCapNhat.add(hd);

                hoaDonChiTietRepository.delete(ct);
                soLuongChiTietHDBiXoa++;
            }

            hoaDonChiTietRepository.flush();

            // Cập nhật lại tổng tiền cho các Hóa đơn tại quầy bị ảnh hưởng
            for (HoaDon hd : dsHoaDonCanCapNhat) {
                List<HoaDonChiTiet> ctConLai = hoaDonChiTietRepository.findByIdHoaDon_Id(hd.getId());

                BigDecimal tongTienHangMoi = ctConLai.stream()
                        .map(HoaDonChiTiet::getThanhTien)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                hd.setTongTienHang(tongTienHangMoi);
                BigDecimal tongGiamGia = hd.getTongGiamGia() != null ? hd.getTongGiamGia() : BigDecimal.ZERO;
                hd.setTongThanhToan(tongTienHangMoi.subtract(tongGiamGia).max(BigDecimal.ZERO));

                hoaDonRepository.save(hd);
            }
        }

        // ================== 2. CHUYỂN TRẠNG THÁI NGỪNG KINH DOANH & BẢO VỆ TỒN ĐƠN ONLINE ==================
        spct.setTrangThai(false); // Chuyển sang Ngừng kinh doanh

        List<String> trangThaiKhongThuongLuong = List.of(
                "da_xac_nhan",
                "cho_van_chuyen",
                "dang_giao"
        );
        Integer soLuongDaXacNhanHoacGiao = hoaDonChiTietRepository
                .sumSoLuongBySpctAndTrangThaiIn(spct.getId(), trangThaiKhongThuongLuong);
        if (soLuongDaXacNhanHoacGiao == null) soLuongDaXacNhanHoacGiao = 0;

        Integer soLuongChoOnline = hoaDonChiTietRepository
                .sumSoLuongBySpctAndTrangThaiAndLoaiHoaDon(spct.getId(), "cho_xac_nhan", "ONLINE");
        if (soLuongChoOnline == null) soLuongChoOnline = 0;

        spct.setSoLuongTamGiu(soLuongDaXacNhanHoacGiao + soLuongChoOnline);

        SanPhamChiTiet updated = sanPhamChiTietRepository.save(spct);

        if (spct.getIdSanPham() != null) {
            capNhatTrangThaiSanPham(spct.getIdSanPham().getId());
        }

        // ================== 3. TẠO THÔNG BÁO ==================
        // ================== 3. TẠO THÔNG BÁO ==================
        String tenSpct = spct.getTenSanPhamChiTiet() != null ? spct.getTenSanPhamChiTiet() : "";

        final String thongBao = (soLuongChiTietHDBiXoa > 0)
                ? "Sản phẩm [" + tenSpct + "] đã chuyển sang NGỪNG KINH DOANH và tự động xóa khỏi các hóa đơn chờ tại quầy!"
                : "Sản phẩm [" + tenSpct + "] đã chuyển sang NGỪNG KINH DOANH!";

        // ================== 4. BẮN SOCKET VỚI EVENT "STOCK_FORCE_ADJUSTED" ==================
        PosAlertEvent alertEvent = new PosAlertEvent("STOCK_FORCE_ADJUSTED", thongBao, updated.getId(), 0);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    try {
                        posSocketService.send(alertEvent);
                    } catch (Exception e) {
                        System.err.println("Lỗi gửi Socket tới POS: " + e.getMessage());
                    }
                }
            });
        } else {
            // Fallback gửi trực tiếp nếu Transaction Synchronization chưa được bật
            try {
                posSocketService.send(alertEvent);
            } catch (Exception e) {
                System.err.println("Lỗi gửi Socket trực tiếp tới POS: " + e.getMessage());
            }
        }

        // ================== 5. TRẢ VỀ KẾT QUẢ ==================
        Map<String, Object> response = new HashMap<>();
        response.put("data", mapToResponse(updated));
        response.put("message", thongBao);

        return response;
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
            res.setSoLuongKhaDung(spct.getSoLuongKhaDung());
            res.setSoLuongTamGiu(spct.getSoLuongTamGiu());

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

        // Lấy số lượng bản ghi hiện tại để làm mốc tăng tự động
        long currentCount = sanPhamChiTietRepository.count();

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

            // Tăng đếm và gán mã tự động: SPCT01, SPCT02,...
            currentCount++;
            spct.setMaSanPhamChiTiet(String.format("SPCT%02d", currentCount));

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
                    res.setMoTa(spct.getIdSanPham().getMoTa());

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
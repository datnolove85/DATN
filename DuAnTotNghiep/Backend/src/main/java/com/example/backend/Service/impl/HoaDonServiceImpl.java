package com.example.backend.Service.impl;

import com.example.backend.Config.WebSocketConfig;
import com.example.backend.Entity.*;
import com.example.backend.Exception.ApiException;

import com.example.backend.Service.CheckoutValidatorService;
import com.example.backend.Service.PosSocketService;
import com.example.backend.Service.TrangThaiRule;
import com.example.backend.Service.payment.VoucherConsumeService;
import com.example.backend.utils.VietQrUtil;
import com.example.backend.Repository.*;
import com.example.backend.Request.*;
import com.example.backend.Response.*;
import com.example.backend.Service.HoaDonService;
import com.example.backend.mapper.HoaDonMapper;
import com.example.backend.secutity.JwtService;
import com.example.backend.specification.HoaDonSpecification;
import com.example.backend.websocket.PosEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonRepository hoaDonRepo;
    private final HoaDonChiTietRepository ctRepo;
    private final SanPhamChiTietRepository spctRepo;
    private final ThanhToanRepository ttRepo;
    private final PhuongThucThanhToanRepository ptRepo;
    private final KhachHangRepository khRepo;
    private final VoucherRepository voucherRepo;
    private final HoaDonVoucherRepository hoaDonVoucherRepo;
    private final TraHangChiTietRepository traHangChiTietRepository;
    private final NhanVienRepository nhanVienRepo;
    private final PosSocketService posSocketService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private HinhAnhRepository hinhAnhRepository;
    private final JwtService jwtService;
    private final KhachHangRepository khachHangRepository;
    private final DiaChiKhachHangRepository diaChiKhachHangRepository;
    private final SanPhamGiamGiaRepository sanPhamGiamGiaRepository;
    private final VoucherCuaKhachHangRepository voucherCuaKhachHangRepo;
    private final KhoVoucherRepository khoVoucherRepository;
    private final VoucherConsumeService voucherConsumeService;



    @Override
    public HoaDon taoHoaDonCho(TaoHoaDonRequest request) {
        NhanVien nhanVien = nhanVienRepo.findById(request.getIdNhanVien())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        HoaDon hoaDon = new HoaDon();
        long count = hoaDonRepo.count() + 1;
        hoaDon.setMaHoaDon(String.format("HD%02d", count));
        hoaDon.setLoaiHoaDon("tai_quay");
        hoaDon.setIdNhanVien(nhanVien);
        hoaDon.setTrangThai("cho_xac_nhan");
        hoaDon.setTongTienHang(BigDecimal.ZERO);
        hoaDon.setTongGiamGia(BigDecimal.ZERO);
        hoaDon.setPhiVanChuyen(BigDecimal.ZERO);
        hoaDon.setTongThanhToan(BigDecimal.ZERO);
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setNgayCapNhat(LocalDateTime.now());
        hoaDon.setTrangThaiThanhToan("chua_thanh_toan");
        return hoaDonRepo.save(hoaDon);
    }

    public HoaDon taoHoaDonChoOnline() {
        HoaDon hoaDon = new HoaDon();
        long count = hoaDonRepo.count() + 1;
        hoaDon.setMaHoaDon(String.format("HD%02d", count));
        hoaDon.setLoaiHoaDon("online");
        hoaDon.setTrangThai("cho_xac_nhan");
        hoaDon.setTongTienHang(BigDecimal.ZERO);
        hoaDon.setTongGiamGia(BigDecimal.ZERO);
        hoaDon.setPhiVanChuyen(BigDecimal.ZERO);
        hoaDon.setTongThanhToan(BigDecimal.ZERO);
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setNgayCapNhat(LocalDateTime.now());
        hoaDon.setTrangThaiThanhToan("chua_thanh_toan");
        return hoaDonRepo.save(hoaDon);
    }


    @Override
    public List<HoaDon> getHoaDonCho(Integer idNhanVien) {

        return hoaDonRepo
                .findByLoaiHoaDonAndTrangThaiAndIdNhanVien_IdOrderByNgayTaoDesc(
                        "tai_quay",
                        "cho_xac_nhan",
                        idNhanVien
                );
    }

    @Override
    public List<HoaDonResponse> getAll() {
        return hoaDonRepo.getAllResponse();
    }

    public HoaDonDetailResponse getDetail(Integer id) {
        HoaDon hd = hoaDonRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hóa đơn"));
        Map<Integer, String> thumbnailMap = new HashMap<>();

        for (Object[] obj : spctRepo.getAllImages()) {

            Integer spctId = (Integer) obj[0];
            String link = (String) obj[1];

            thumbnailMap.putIfAbsent(spctId, link);
        }
        List<HoaDonChiTietResponse> sanPhams =
                ctRepo.findByHoaDon(id)
                        .stream()
                        .map(ct -> {

                            SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();

                            String anh = thumbnailMap.get(spct.getId());

                            if (anh != null) {
                                anh = "/sanpham/" + anh;
                            }

                            return new HoaDonChiTietResponse(
                                    ct.getId(),
                                    spct.getId(),
                                    spct.getMaSanPhamChiTiet(),
                                    spct.getIdSanPham().getTenSanPham(),
                                    spct.getIdMauSac().getTenMauSac(),
                                    spct.getIdKichThuoc().getTenKichThuoc(),
                                    spct.getIdSanPham().getIdThuongHieu().getTenThuongHieu(),
                                    ct.getSoLuong(),
                                    ct.getDonGia(),
                                    ct.getThanhTien(),
                                    anh
                            );
                        })
                        .toList();

        String phuongThuc = "";
        Optional<ThanhToan> thanhToan =
                ttRepo.findFirstByIdHoaDon_Id(id);
        if (thanhToan.isPresent()) {
            phuongThuc =
                    thanhToan.get()
                            .getIdPhuongThucThanhToan()
                            .getTenPhuongThuc();
        }

        HoaDonDetailResponse response =
                new HoaDonDetailResponse();
        response.setId(hd.getId());
        response.setMaHoaDon(hd.getMaHoaDon());

        if (hd.getIdKhachHang() != null) {
            response.setIdKhachHang(
                    hd.getIdKhachHang().getId());
            response.setTenKhachHang(
                    hd.getIdKhachHang().getHoTen());
            response.setSoDienThoaiKhachHang(
                    hd.getIdKhachHang().getSoDienThoai());
        }


        response.setTenNguoiNhan(
                hd.getTenNguoiNhan());
        response.setSoDienThoaiNguoiNhan(
                hd.getSoDienThoaiNguoiNhan());
        response.setDiaChiGiaoHang(
                hd.getDiaChiGiaoHang());
        response.setLoaiHoaDon(
                hd.getLoaiHoaDon());
        response.setTrangThai(
                hd.getTrangThai());
        response.setGhiChu(
                hd.getGhiChu());
        response.setTongTienHang(
                hd.getTongTienHang());
        response.setTongGiamGia(
                hd.getTongGiamGia());
        response.setPhiVanChuyen(
                hd.getPhiVanChuyen());
        response.setTongThanhToan(
                hd.getTongThanhToan());
        response.setNgayTao(
                hd.getNgayTao());
        response.setNgayCapNhat(
                hd.getNgayCapNhat());
        response.setPhuongThucThanhToan(
                phuongThuc);
        response.setSanPhams(
                sanPhams);

        Optional<Voucher> voucherOpt =
                hoaDonVoucherRepo.findVoucherByHoaDonId(id);

        if (voucherOpt.isPresent()) {
            response.setVoucher(voucherOpt.get());
        }
        return response;
    }

    // ================= FILTER =================
    @Override
    public List<HoaDonResponse> filter(String trangThai, String loaiHoaDon) {
        return hoaDonRepo.getAllFromView()
                .stream()
                .map(HoaDonMapper::fromViewRow)
                .filter(x ->
                        (trangThai == null || x.getTrangThai().equals(trangThai)) &&
                                (loaiHoaDon == null || x.getLoaiHoaDon().equals(loaiHoaDon))
                )
                .collect(Collectors.toList());
    }

    public Page<HoaDon> search(HoaDonFilterRequest req, Pageable pageable) {

        return hoaDonRepo.findAll(
                HoaDonSpecification.filter(req),
                pageable
        );
    }

    // ================= SEARCH =================
    @Override
    public List<HoaDonResponse> search(String keyword) {
        return hoaDonRepo.getAllFromView()
                .stream()
                .map(HoaDonMapper::fromViewRow)
                .filter(x ->
                        x.getMaHoaDon().contains(keyword) ||
                                x.getTenNguoiNhan().contains(keyword) ||
                                x.getSoDienThoaiNguoiNhan().contains(keyword)
                )
                .collect(Collectors.toList());
    }

    @Override
    public Page<HoaDonResponse> searchOnline(
            HoaDonFilterRequest req,
            Pageable pageable
    ) {

        // luôn là hóa đơn online
        req.setLoaiHoaDon("online");

        Page<HoaDon> page = hoaDonRepo.findAll(
                HoaDonSpecification.filter(req),
                pageable
        );

        return page.map(hd -> {

            HoaDonResponse res = new HoaDonResponse();

            res.setId(hd.getId());
            res.setIdKhachHang(
                    hd.getIdKhachHang() != null
                            ? hd.getIdKhachHang().getId()
                            : null
            );
            res.setMaHoaDon(hd.getMaHoaDon());
            res.setTongTienHang(hd.getTongTienHang());
            res.setTongGiamGia(hd.getTongGiamGia());
            res.setPhiVanChuyen(hd.getPhiVanChuyen());
            res.setTongThanhToan(hd.getTongThanhToan());

            res.setTenNguoiNhan(hd.getTenNguoiNhan());
            res.setSoDienThoaiNguoiNhan(hd.getSoDienThoaiNguoiNhan());
            res.setDiaChiGiaoHang(hd.getDiaChiGiaoHang());

            res.setLoaiHoaDon(hd.getLoaiHoaDon());
            res.setTrangThai(hd.getTrangThai());
            res.setTrangThaiThanhToan(hd.getTrangThaiThanhToan());

            res.setNgayTao(hd.getNgayTao());
            res.setNgayCapNhat(hd.getNgayCapNhat());
            res.setGhiChu(hd.getGhiChu());
            ttRepo
                    .findFirstByIdHoaDon_IdOrderByNgayThanhToanDesc(hd.getId())
                    .ifPresent(thanhToan -> {

                        PhuongThucThanhToan phuongThuc =
                                thanhToan.getIdPhuongThucThanhToan();

                        if (phuongThuc != null) {
                            res.setPhuongThucThanhToan(
                                    phuongThuc.getTenPhuongThuc()
                            );
                        }
                    });

            return res;
        });
    }

    // ================= PAGINATION =================
    @Override
    public Page<HoaDonResponse> getAll(Pageable pageable) {
        List<HoaDonResponse> all = hoaDonRepo.getAllFromView()
                .stream()
                .map(HoaDonMapper::fromViewRow)
                .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), all.size());
        List<HoaDonResponse> pageContent = all.subList(start, end);
        return new PageImpl<>(pageContent, pageable, all.size());
    }


        @Override
        @Transactional(noRollbackFor = ApiException.class) // 🔴 BẮT BUỘC: Giữ lại cập nhật DB khi văng ApiException
        public Object thanhToanHoaDon(ThanhToanHoaDonRequest req) {

            // ================== 1. KIỂM TRA HÓA ĐƠN & DANH SÁCH CHI TIẾT ==================
            HoaDon hd = hoaDonRepo.findById(req.getIdHoaDon())
                    .orElseThrow(() -> new ApiException("Không tìm thấy hóa đơn!", "HOA_DON_NOT_FOUND"));

            if (!"cho_xac_nhan".equals(hd.getTrangThai())) {
                throw new ApiException("Hóa đơn đã được thanh toán hoặc không hợp lệ!", "TRANG_THAI_INVALID");
            }

            List<HoaDonChiTiet> dsChiTiet = ctRepo.findByIdHoaDon_Id(hd.getId());
            if (dsChiTiet.isEmpty()) {
                throw new ApiException("Hóa đơn chưa có sản phẩm nào, không thể thanh toán!", "HOA_DON_EMPTY");
            }

            // ================== 2. KIỂM TRA TRẠNG THÁI & TỒN KHO KHẢ DỤNG ==================
            List<HoaDonChiTiet> listXoaKhoiHoaDon = new ArrayList<>();
            List<String> thongBaoDieuChinh = new ArrayList<>();
            boolean coDieuChinhHoaDon = false;

            for (HoaDonChiTiet ct : dsChiTiet) {
                SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();
                if (spct == null) {
                    listXoaKhoiHoaDon.add(ct);
                    coDieuChinhHoaDon = true;
                    continue;
                }

                SanPham spCha = spct.getIdSanPham();
                String tenSp = (spCha != null ? spCha.getTenSanPham() : "") + " (" + spct.getMaSanPhamChiTiet() + ")";

                int soLuongMua = Optional.ofNullable(ct.getSoLuong()).orElse(0);
                int tonKhaDung = Optional.ofNullable(spct.getSoLuongTon()).orElse(0); // Tồn kho thực tế
                boolean isNgungKinhDoanh = (spCha != null && Boolean.FALSE.equals(spCha.getTrangThai()))
                        || Boolean.FALSE.equals(spct.getTrangThai());

                // TH1: Sản phẩm bị ngừng kinh doanh -> Gỡ khỏi hóa đơn
                if (isNgungKinhDoanh) {
                    listXoaKhoiHoaDon.add(ct);
                    thongBaoDieuChinh.add("Sản phẩm [" + tenSp + "] đã NGỪNG KINH DOANH (đã tự động gỡ khỏi hóa đơn)");
                    giaiPhongTamGiu(spct, soLuongMua);
                    coDieuChinhHoaDon = true;
                    continue;
                }

                // Cập nhật giá bán mới nhất tại thời điểm thanh toán
                BigDecimal giaMoi = tinhGiaBanHienTai(spct);
                ct.setDonGia(giaMoi);

                // TH2: KIỂM TRA SỐ LƯỢNG TỒN KHẢ DỤNG
                if (tonKhaDung < soLuongMua) {
                    coDieuChinhHoaDon = true;

                    if (tonKhaDung <= 0) {
                        // A. Hết hàng hoàn toàn (Tồn = 0) -> Gỡ khỏi hóa đơn
                        listXoaKhoiHoaDon.add(ct);
                        thongBaoDieuChinh.add("Sản phẩm [" + tenSp + "] đã HẾT HÀNG (đã tự động gỡ khỏi hóa đơn)");
                        giaiPhongTamGiu(spct, soLuongMua);
                    } else {
                        // B. Không đủ hàng (Tồn < Mua) -> Cập nhật về đúng số lượng khả dụng
                        ct.setSoLuong(tonKhaDung);
                        ct.setThanhTien(giaMoi.multiply(BigDecimal.valueOf(tonKhaDung)));

                        // Trả lại lượng tạm giữ chênh lệch
                        giaiPhongTamGiu(spct, soLuongMua - tonKhaDung);

                        thongBaoDieuChinh.add("Sản phẩm [" + tenSp + "] không đủ số lượng (chỉ còn " + tonKhaDung
                                + "), đã tự động điều chỉnh về " + tonKhaDung);
                    }
                } else {
                    // C. Đủ hàng -> Tính lại thành tiền bình thường
                    ct.setThanhTien(giaMoi.multiply(BigDecimal.valueOf(soLuongMua)));
                }
            }

            // ================== 3. XỬ LÝ KHI CÓ ĐIỀU CHỈNH (XÓA / GIẢM SỐ LƯỢNG) ==================
            if (coDieuChinhHoaDon) {
                if (!listXoaKhoiHoaDon.isEmpty()) {
                    ctRepo.deleteAll(listXoaKhoiHoaDon);
                    dsChiTiet.removeAll(listXoaKhoiHoaDon);
                }

                ctRepo.saveAll(dsChiTiet);
                recalculateHoaDon(hd.getId());

                if (dsChiTiet.isEmpty()) {
                    throw new ApiException("Tất cả sản phẩm trong hóa đơn đã hết hàng hoặc ngừng kinh doanh!", "HOA_DON_EMPTY");
                }

                String fullMessage = "Số lượng trong kho không đủ! Chi tiết:\n• "
                        + String.join("\n• ", thongBaoDieuChinh)
                        + "\n\n--> Hóa đơn đã được cập nhật lại, vui lòng kiểm tra lại trước khi bấm thanh toán!";

                throw new ApiException(fullMessage, "INVOICE_QUANTITY_ADJUSTED");
            }

            ctRepo.saveAll(dsChiTiet);

            // ================== 4. ĐỒNG BỘ VOUCHER & PHÂN BỔ GIÁ ==================
            recalculateHoaDon(hd.getId());


                kiemTraVoucherConHopLe(hd.getId());


            hd = hoaDonRepo.findById(hd.getId()).orElseThrow();
            BigDecimal tongTienHang = hd.getTongTienHang();
            BigDecimal tongThanhToan = hd.getTongThanhToan();

            if (tongTienHang.compareTo(BigDecimal.ZERO) > 0 && tongTienHang.compareTo(tongThanhToan) > 0) {
                BigDecimal tongDaPhanBo = BigDecimal.ZERO;

                for (int i = 0; i < dsChiTiet.size(); i++) {
                    HoaDonChiTiet ct = dsChiTiet.get(i);
                    if (i == dsChiTiet.size() - 1) {
                        ct.setThanhTien(tongThanhToan.subtract(tongDaPhanBo));
                    } else {
                        BigDecimal thanhTienMoi = ct.getThanhTien()
                                .multiply(tongThanhToan)
                                .divide(tongTienHang, 2, RoundingMode.HALF_UP);
                        ct.setThanhTien(thanhTienMoi);
                        tongDaPhanBo = tongDaPhanBo.add(thanhTienMoi);
                    }
                }
                ctRepo.saveAll(dsChiTiet);
            }

            // ================== 5. TRỪ KHO THỰC TẾ & BẮN SOCKET CẬP NHẬT POS ==================
            for (HoaDonChiTiet ct : dsChiTiet) {
                SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();
                int soLuongMua = Optional.ofNullable(ct.getSoLuong()).orElse(0);
                int tonHienTai = Optional.ofNullable(spct.getSoLuongTon()).orElse(0);
                int tamGiuHienTai = Optional.ofNullable(spct.getSoLuongTamGiu()).orElse(0);

                spct.setSoLuongTon(Math.max(0, tonHienTai - soLuongMua));
                spct.setSoLuongTamGiu(Math.max(0, tamGiuHienTai - soLuongMua));
                spctRepo.save(spct);
            }

            // ================== 6. TẠO THANH TOÁN & XỬ LÝ THANH TOÁN ==================
            List<ThanhToanHoaDonRequest.ChiTietThanhTienDto> danhSachThanhToanXuLy = new ArrayList<>();

            if (req.getDanhSachThanhToan() != null && !req.getDanhSachThanhToan().isEmpty()) {
                danhSachThanhToanXuLy = req.getDanhSachThanhToan();
            } else if (req.getIdPhuongThucThanhToan() != null) {
                ThanhToanHoaDonRequest.ChiTietThanhTienDto dtoCu = new ThanhToanHoaDonRequest.ChiTietThanhTienDto();
                dtoCu.setIdPhuongThucThanhToan(req.getIdPhuongThucThanhToan());
                dtoCu.setSoTien(tongThanhToan);
                dtoCu.setMaGiaoDich(null);
                danhSachThanhToanXuLy.add(dtoCu);
            } else {
                throw new ApiException("Vui lòng chọn phương thức thanh toán!", "PAYMENT_METHOD_REQUIRED");
            }

            BigDecimal tongTienKhachTra = danhSachThanhToanXuLy.stream()
                    .map(ThanhToanHoaDonRequest.ChiTietThanhTienDto::getSoTien)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (tongTienKhachTra.compareTo(tongThanhToan) < 0) {
                throw new ApiException("Số tiền thanh toán (" + tongTienKhachTra + ") chưa đủ so với tổng tiền hóa đơn (" + tongThanhToan + ")!", "INVALID_PAYMENT_AMOUNT");
            }

            List<PhuongThucThanhToan> dsPhuongThucTraVe = new ArrayList<>();

            for (ThanhToanHoaDonRequest.ChiTietThanhTienDto item : danhSachThanhToanXuLy) {
                PhuongThucThanhToan pt = ptRepo.findById(item.getIdPhuongThucThanhToan())
                        .orElseThrow(() -> new ApiException("Phương thức thanh toán không hợp lệ!", "PAYMENT_METHOD_NOT_FOUND"));

                ThanhToan tt = new ThanhToan();
                tt.setIdHoaDon(hd);
                tt.setIdPhuongThucThanhToan(pt);
                tt.setSoTien(item.getSoTien());
                tt.setMaGiaoDich(item.getMaGiaoDich());
                tt.setTrangThai("da_thanh_toan");
                tt.setNgayThanhToan(LocalDateTime.now());
                ttRepo.save(tt);

                dsPhuongThucTraVe.add(pt);
            }

            BigDecimal tienKhachDuaReq = req.getTienKhachDua() != null ? req.getTienKhachDua() : tongThanhToan;

            // Nếu khách đưa tiền mặt thừa, tính tiền thối lại. Nếu không thì tiền thối = 0
            BigDecimal tienThoiLai = tienKhachDuaReq.compareTo(tongThanhToan) > 0
                    ? tienKhachDuaReq.subtract(tongThanhToan)
                    : BigDecimal.ZERO;

            // Set vào Entity HoaDon
            hd.setTienKhachDua(tienKhachDuaReq);
            hd.setTienThoiLai(tienThoiLai);

            // ================== 7. XỬ LÝ VOUCHER (CÁ NHÂN & HỆ THỐNG) ==================
            // ================== 7. TIÊU THỤ VOUCHER ==================
            voucherConsumeService.consumeVoucher(hd.getId());
            // ================== 8. CHỐT TRẠNG THÁI HÓA ĐƠN & BẮN SOCKET ==================
            hd.setTrangThai("hoan_thanh");
            hd.setNgayCapNhat(LocalDateTime.now());
            hd.setTrangThaiThanhToan("da_thanh_toan");
            hoaDonRepo.save(hd);

            posSocketService.send(new PosEvent("INVOICE_PAID", hd.getId(), null, null));

            // ================== 9. TRẢ VỀ DỮ LIỆU THÀNH CÔNG ==================
            return buildResponseData(hd, dsPhuongThucTraVe.get(0), dsChiTiet);
        }

// ==================================================================================
// HÀM HELPER DÙNG TRONG THANH TOÁN
// ==================================================================================



    // 2. Build dữ liệu Response trả về cho Frontend
    private Map<String, Object> buildResponseData(HoaDon hd, PhuongThucThanhToan pt, List<HoaDonChiTiet> dsChiTiet) {
        Map<String, Object> res = new HashMap<>();
        List<Map<String, Object>> sanPhams = new ArrayList<>();

        for (HoaDonChiTiet ct : dsChiTiet) {
            SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();
            SanPham sp = spct.getIdSanPham();

            Map<String, Object> item = new HashMap<>();
            item.put("tenSanPham", sp != null ? sp.getTenSanPham() : "");
            item.put("maSanPhamChiTiet", spct.getMaSanPhamChiTiet());
            item.put("tenChatLieu", sp != null && sp.getIdChatLieu() != null ? sp.getIdChatLieu().getTenChatLieu() : "");
            item.put("tenThuongHieu", sp != null && sp.getIdThuongHieu() != null ? sp.getIdThuongHieu().getTenThuongHieu() : "");
            item.put("soLuong", ct.getSoLuong());
            item.put("donGia", ct.getDonGia());
            item.put("thanhTien", ct.getThanhTien());
            item.put("tenMauSac", spct.getIdMauSac() != null ? spct.getIdMauSac().getTenMauSac() : "");
            item.put("tenKichThuoc", spct.getIdKichThuoc() != null ? spct.getIdKichThuoc().getTenKichThuoc() : "");
            sanPhams.add(item);
        }

        res.put("id", hd.getId());
        res.put("maHoaDon", hd.getMaHoaDon());
        res.put("tongTienHang", hd.getTongTienHang());
        res.put("tongGiamGia", hd.getTongGiamGia());
        res.put("tongThanhToan", hd.getTongThanhToan());
        res.put("ngayTao", hd.getNgayTao());
        res.put("phuongThucThanhToan", pt.getTenPhuongThuc());

        if (hd.getIdKhachHang() != null) {
            res.put("tenKhachHang", hd.getIdKhachHang().getHoTen());
            res.put("soDienThoai", hd.getIdKhachHang().getSoDienThoai());
        }

        res.put("sanPhams", sanPhams);
        return res;
    }

    // ---------------- Helper Method: Giải phóng tạm giữ ----------------
    private void giaiPhongTamGiu(SanPhamChiTiet spct, int soLuongGiam) {
        if (soLuongGiam <= 0) return;
        int tamGiuHienTai = Optional.ofNullable(spct.getSoLuongTamGiu()).orElse(0);
        spct.setSoLuongTamGiu(Math.max(0, tamGiuHienTai - soLuongGiam));
        spctRepo.save(spct);
    }

    // Helper tính giá khuyến mãi
    private BigDecimal tinhGiaBanHienTai(SanPhamChiTiet spct) {
        BigDecimal giaApDung = spct.getGiaBan();
        Optional<SanPhamGiamGia> spggOpt = sanPhamGiamGiaRepository.findDangGiamGiaBySpctId(spct.getId());

        if (spggOpt.isPresent()) {
            DotGiamGia dot = spggOpt.get().getDotGiamGia();

            if ("phan_tram".equals(dot.getLoaiGiamGia())) {
                BigDecimal tienGiam = giaApDung.multiply(dot.getGiaTriGiam())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                if (dot.getGiaTriGiamToiDa() != null && tienGiam.compareTo(dot.getGiaTriGiamToiDa()) > 0) {
                    tienGiam = dot.getGiaTriGiamToiDa();
                }
                giaApDung = giaApDung.subtract(tienGiam);
            } else {
                giaApDung = giaApDung.subtract(dot.getGiaTriGiam());
                if (giaApDung.compareTo(BigDecimal.ZERO) < 0) {
                    giaApDung = BigDecimal.ZERO;
                }
            }
        }
        return giaApDung;
    }
    @Override
    @Transactional
    public Object thanhToanHoaDonOnline(
            ThanhToanHoaDonRequest req
    ) {

        HoaDon hd = hoaDonRepo.findById(
                req.getIdHoaDon()
        ).orElseThrow(() ->
                new RuntimeException("Không tìm thấy hóa đơn"));

        if (!"cho_xac_nhan".equals(
                hd.getTrangThai())) {

            throw new RuntimeException(
                    "Hóa đơn đã được thanh toán");
        }

        List<HoaDonChiTiet> dsChiTiet =
                ctRepo.findByIdHoaDon_Id(
                        hd.getId());

        if (dsChiTiet.isEmpty()) {

            throw new RuntimeException(
                    "Hóa đơn chưa có sản phẩm");
        }

        // Tính lại toàn bộ tiền
        recalculateHoaDon(
                hd.getId());

        // lấy lại hóa đơn mới nhất
        hd = hoaDonRepo.findById(
                hd.getId()
        ).orElseThrow();

        BigDecimal tongTienHang =
                hd.getTongTienHang();

        BigDecimal tongGiamGia =
                hd.getTongGiamGia();

        BigDecimal tongThanhToan =
                hd.getTongThanhToan();

        // phương thức thanh toán
        PhuongThucThanhToan pt =
                ptRepo.findById(
                        req.getIdPhuongThucThanhToan()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy phương thức"));

        // tạo thanh toán
        ThanhToan tt =
                new ThanhToan();

        tt.setIdHoaDon(hd);

        tt.setIdPhuongThucThanhToan(
                pt);

        tt.setSoTien(
                tongThanhToan);

        if ("COD".equals(pt.getMaPhuongThuc())) {
            tt.setTrangThai("chua_thanh_toan");
        } else {
            tt.setTrangThai("da_thanh_toan");
            tt.setNgayThanhToan(LocalDateTime.now());
        }

        tt.setNgayThanhToan(
                LocalDateTime.now());

        ttRepo.save(tt);

        // tăng lượt dùng voucher
        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo
                        .findByIdHoaDon_Id(
                                hd.getId())
                        .orElse(null);

        if (hdVoucher != null) {

            Voucher voucher =
                    hdVoucher.getIdVoucher();

            voucher.setSoLuongDaDung(
                    voucher.getSoLuongDaDung() + 1);

            voucherRepo.save(
                    voucher);
        }

        // cập nhật hóa đơn
        // cập nhật trạng thái hóa đơn
        if ("COD".equals(pt.getMaPhuongThuc())) {

            // COD: chờ shop xác nhận
            hd.setTrangThai("cho_xac_nhan");
            hd.setTrangThaiThanhToan("chua_thanh_toan");

        } else {

            // Chuyển khoản: đã thanh toán => xác nhận luôn
            hd.setTrangThaiThanhToan("da_thanh_toan");
            hd.setTrangThai("da_xac_nhan");

        }

        hd.setNgayCapNhat(LocalDateTime.now());
        hoaDonRepo.save(hd);
        hd.setNgayCapNhat(
                LocalDateTime.now());

        hoaDonRepo.save(
                hd);

        Map<String, Object> res =
                new HashMap<>();
        List<Map<String, Object>> sanPhams = new ArrayList<>();
        for (HoaDonChiTiet ct : dsChiTiet) {
            Map<String, Object> sp = new HashMap<>();
            sp.put("tenSanPham", ct.getIdSanPhamChiTiet().getIdSanPham().getTenSanPham());
            sp.put("maSanPhamChiTiet", ct.getIdSanPhamChiTiet().getMaSanPhamChiTiet());
            sp.put("tenChatLieu", ct.getIdSanPhamChiTiet().getIdSanPham().getIdChatLieu().getTenChatLieu());
            sp.put("tenThuongHieu", ct.getIdSanPhamChiTiet().getIdSanPham().getIdThuongHieu().getTenThuongHieu());
            sp.put("soLuong", ct.getSoLuong());
            sp.put("donGia", ct.getDonGia());
            sp.put("tenMauSac", ct.getIdSanPhamChiTiet().getIdMauSac().getTenMauSac());
            sp.put("tenKichThuoc", ct.getIdSanPhamChiTiet().getIdKichThuoc().getTenKichThuoc());
            sanPhams.add(sp);
        }
        res.put("id", hd.getId());
        res.put("maHoaDon", hd.getMaHoaDon());
        res.put("tongTienHang", tongTienHang);
        res.put("tongGiamGia", tongGiamGia);
        res.put("tongThanhToan", tongThanhToan);
        res.put("ngayTao", hd.getNgayTao());
        res.put("phuongThucThanhToan", pt.getTenPhuongThuc());
        if (hd.getIdKhachHang() != null) {
            res.put("tenKhachHang", hd.getIdKhachHang().getHoTen());
            res.put("soDienThoai", hd.getIdKhachHang().getSoDienThoai());
        }
        res.put("sanPhams", sanPhams);
        return res;
    }


    @Transactional
    @Override
    public void themSanPhamVaoHoaDon(
            ThemSanPhamRequest request
    ) {

        HoaDon hoaDon =
                hoaDonRepo.findById(
                        request.getIdHoaDon()
                ).orElseThrow(
                        () -> new RuntimeException("Không tìm thấy hóa đơn")
                );


        SanPhamChiTiet spct =
                spctRepo.findById(
                        request.getIdSanPhamChiTiet()
                ).orElseThrow(
                        () -> new RuntimeException("Không tìm thấy sản phẩm")
                );


        // Giá mặc định là giá gốc
        BigDecimal giaApDung =
                spct.getGiaBan();


        // Kiểm tra sản phẩm đang có đợt giảm giá không
        Optional<SanPhamGiamGia> spggOpt =
                sanPhamGiamGiaRepository.findDangGiamGiaBySpctId(
                        spct.getId()
                );


        if (spggOpt.isPresent()) {

            DotGiamGia dot =
                    spggOpt.get()
                            .getDotGiamGia();

            // Giảm theo phần trăm
            if ("phan_tram".equals(dot.getLoaiGiamGia())) {

                BigDecimal tienGiam =
                        giaApDung
                                .multiply(
                                        dot.getGiaTriGiam()
                                )
                                .divide(
                                        BigDecimal.valueOf(100)
                                );

                // Có giới hạn giảm tối đa
                if (dot.getGiaTriGiamToiDa() != null
                        &&
                        tienGiam.compareTo(
                                dot.getGiaTriGiamToiDa()
                        ) > 0) {

                    tienGiam =
                            dot.getGiaTriGiamToiDa();

                }

                giaApDung =
                        giaApDung.subtract(
                                tienGiam
                        );

            }
            // Giảm tiền mặt
            else {

                giaApDung =
                        giaApDung.subtract(
                                dot.getGiaTriGiam()
                        );

                if (giaApDung.compareTo(BigDecimal.ZERO) < 0) {

                    giaApDung =
                            BigDecimal.ZERO;

                }

            }

        }


        hoaDon.setNgayCapNhat(
                LocalDateTime.now()
        );


        hoaDonRepo.save(hoaDon);


        // === 1. KIỂM TRA SỐ LƯỢNG KHẢ DỤNG ===
        int ton = spct.getSoLuongTon() != null ? spct.getSoLuongTon() : 0;
        int tamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;
        int khaDung = ton - tamGiu;

        if (khaDung < request.getSoLuong()) {
            throw new RuntimeException("Sản phẩm không đủ số lượng khả dụng để bán");
        }


        Optional<HoaDonChiTiet> hdctOpt =
                ctRepo.findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(
                        hoaDon.getId(),
                        spct.getId()
                );


        // Sản phẩm đã tồn tại trong hóa đơn
        if (hdctOpt.isPresent()) {

            HoaDonChiTiet hdct =
                    hdctOpt.get();

            hdct.setDonGia(
                    giaApDung
            );

            hdct.setSoLuong(
                    hdct.getSoLuong()
                            + request.getSoLuong()
            );

            hdct.setThanhTien(
                    hdct.getDonGia()
                            .multiply(
                                    BigDecimal.valueOf(
                                            hdct.getSoLuong()
                                    )
                            )
            );

            ctRepo.save(hdct);

        }
        // Chưa có trong hóa đơn
        else {

            HoaDonChiTiet hdct =
                    new HoaDonChiTiet();

            hdct.setIdHoaDon(
                    hoaDon
            );

            hdct.setIdSanPhamChiTiet(
                    spct
            );

            hdct.setSoLuong(
                    request.getSoLuong()
            );
            hdct.setGiaNhap(spct.getGiaNhap());

            hdct.setDonGia(
                    giaApDung
            );

            hdct.setThanhTien(
                    giaApDung.multiply(
                            BigDecimal.valueOf(
                                    request.getSoLuong()
                            )
                    )
            );

            ctRepo.save(hdct);

        }


        // === 2. TĂNG SỐ LƯỢNG TẠM GIỮ (KHO THỰC GIỮ NGUYÊN) ===
        spct.setSoLuongTamGiu(tamGiu + request.getSoLuong());

        spctRepo.save(spct);


        recalculateHoaDon(
                hoaDon.getId()
        );


        kiemTraVoucherConHopLe(
                hoaDon.getId()
        );

    }

    @Transactional
    @Override
    public void giamSoLuong(Integer idHoaDonChiTiet) {

        HoaDonChiTiet hdct = ctRepo.findById(idHoaDonChiTiet)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (hdct.getSoLuong() <= 1) {
            throw new RuntimeException("Không thể giảm thêm");
        }

        hdct.setSoLuong(hdct.getSoLuong() - 1);

        hdct.setThanhTien(
                hdct.getDonGia()
                        .multiply(BigDecimal.valueOf(hdct.getSoLuong()))
        );

        ctRepo.save(hdct);

        // === GIẢM SỐ LƯỢNG TẠM GIỮ (KHO THỰC GIỮ NGUYÊN) ===
        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();
        int tamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;
        spct.setSoLuongTamGiu(Math.max(0, tamGiu - 1));

        spctRepo.save(spct);

        HoaDon hoaDon = hdct.getIdHoaDon();

        hoaDon.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hoaDon);

        recalculateHoaDon(hdct.getIdHoaDon().getId());

        kiemTraVoucherConHopLe(hoaDon.getId());

        // ===== Gửi WebSocket =====
        posSocketService.send(
                new PosEvent(
                        "QUANTITY_UPDATED",
                        hdct.getIdHoaDon().getId(),
                        spct.getId(),
                        spct.getSoLuongTon() - spct.getSoLuongTamGiu()
                )
        );
    }

    @Transactional
    @Override
    public void tangSoLuong(Integer idHoaDonChiTiet) {

        HoaDonChiTiet hdct = ctRepo.findById(idHoaDonChiTiet)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

        // === KIỂM TRA SỐ LƯỢNG KHẢ DỤNG ===
        int ton = spct.getSoLuongTon() != null ? spct.getSoLuongTon() : 0;
        int tamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;
        int khaDung = ton - tamGiu;

        if (khaDung <= 0) {
            throw new RuntimeException("Sản phẩm không đủ số lượng khả dụng để bán");
        }

        // Tăng số lượng trong hóa đơn
        hdct.setSoLuong(hdct.getSoLuong() + 1);

        hdct.setThanhTien(
                hdct.getDonGia()
                        .multiply(BigDecimal.valueOf(hdct.getSoLuong()))
        );

        ctRepo.save(hdct);

        // === TĂNG SỐ LƯỢNG TẠM GIỮ (KHO THỰC GIỮ NGUYÊN) ===
        spct.setSoLuongTamGiu(tamGiu + 1);
        spctRepo.save(spct);

        recalculateHoaDon(hdct.getIdHoaDon().getId());
        kiemTraVoucherConHopLe(hdct.getIdHoaDon().getId());

        // Cập nhật tổng tiền hóa đơn
        HoaDon hoaDon = hdct.getIdHoaDon();

        hoaDon.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hoaDon);

        // Gửi WebSocket
        posSocketService.send(
                new PosEvent(
                        "QUANTITY_UPDATED",
                        hdct.getIdHoaDon().getId(),
                        spct.getId(),
                        ton - spct.getSoLuongTamGiu()
                )
        );
    }

    @Transactional
    @Override
    public void huyHoaDon(Integer idHoaDon) {
        // 1. Lấy hóa đơn
        HoaDon hoaDon = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        // 2. Chặn nếu đã thanh toán
        if ("hoan_thanh".equalsIgnoreCase(hoaDon.getTrangThai())) {
            throw new RuntimeException("Không thể hủy hóa đơn đã thanh toán");
        }

        // 3. Lấy danh sách chi tiết hóa đơn
        List<HoaDonChiTiet> listCT =
                ctRepo.findByIdHoaDon_Id(idHoaDon);

        // 4. GIẢM TẠM GIỮ -> HOÀN KHẢ DỤNG (KHO THỰC GIỮ NGUYÊN)
        for (HoaDonChiTiet hdct : listCT) {

            SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

            int tamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;
            int soLuongCanGiaiPhong = hdct.getSoLuong() != null ? hdct.getSoLuong() : 0;

            // Trừ lượng tạm giữ (không để âm)
            spct.setSoLuongTamGiu(Math.max(0, tamGiu - soLuongCanGiaiPhong));

            spctRepo.save(spct);
        }

        // 5. KHÔNG XÓA HDCT (giữ lịch sử)
        // 6. CẬP NHẬT HÓA ĐƠN
        hoaDon.setTrangThai("da_huy");

        hoaDon.setTongThanhToan(BigDecimal.ZERO);
        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElse(null);
        if (hdVoucher != null) {
            hoaDonVoucherRepo.delete(hdVoucher);
        }
        hoaDonRepo.save(hoaDon);
    }

    @Transactional
    @Override
    public void xoaSanPhamKhoiHoaDon(Integer idHoaDonChiTiet) {

        HoaDonChiTiet hdct =
                ctRepo.findById(idHoaDonChiTiet)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy sản phẩm"));

        // === GIẢM SỐ LƯỢNG TẠM GIỮ (KHO THỰC GIỮ NGUYÊN) ===
        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();
        int tamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;
        int soLuongXoa = hdct.getSoLuong() != null ? hdct.getSoLuong() : 0;

        // Giảm số lượng tạm giữ, dùng Math.max để tránh âm
        spct.setSoLuongTamGiu(Math.max(0, tamGiu - soLuongXoa));

        spctRepo.save(spct);

        Integer idHoaDon = hdct.getIdHoaDon().getId();

        ctRepo.delete(hdct);
        removeVoucherIfExists(
                hdct.getIdHoaDon().getId()
        );
        recalculateHoaDon(idHoaDon);

        // ===== Gửi WebSocket =====
        posSocketService.send(
                new PosEvent(
                        "QUANTITY_UPDATED",
                        idHoaDon,
                        spct.getId(),
                        spct.getSoLuongTon() - spct.getSoLuongTamGiu()
                )
        );
    }

    @Override
    @Transactional
    public void capNhatSoLuong(Integer idHoaDonChiTiet, Integer soLuongMoi) {

        System.out.println("\n========== BẮT ĐẦU CẬP NHẬT SỐ LƯỢNG ==========");

        if (soLuongMoi == null || soLuongMoi <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        HoaDonChiTiet hdct = ctRepo.findById(idHoaDonChiTiet)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết hóa đơn"));

        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

        int soLuongCu = hdct.getSoLuong();

        if (soLuongMoi == soLuongCu) {
            System.out.println("=> Không có thay đổi số lượng.");
            return;
        }

        int chenhLech = soLuongMoi - soLuongCu;

        int ton = spct.getSoLuongTon() == null ? 0 : spct.getSoLuongTon();
        int tamGiu = spct.getSoLuongTamGiu() == null ? 0 : spct.getSoLuongTamGiu();
        int khaDung = ton - tamGiu;

        System.out.println("Tồn: " + ton);
        System.out.println("Tạm giữ trước: " + tamGiu);
        System.out.println("Khả dụng: " + khaDung);
        System.out.println("Số lượng cũ: " + soLuongCu);
        System.out.println("Số lượng mới: " + soLuongMoi);
        System.out.println("Chênh lệch: " + chenhLech);

        // Nếu tăng số lượng thì kiểm tra tồn
        if (chenhLech > 0 && khaDung < chenhLech) {
            throw new RuntimeException("Số lượng khả dụng không đủ (Còn lại: " + khaDung + ")");
        }

        // Cập nhật số lượng hóa đơn
        hdct.setSoLuong(soLuongMoi);
        hdct.setThanhTien(
                hdct.getDonGia().multiply(BigDecimal.valueOf(soLuongMoi))
        );

        // Cập nhật số lượng tạm giữ
        spct.setSoLuongTamGiu(tamGiu + chenhLech);

        System.out.println("Tạm giữ sau: " + spct.getSoLuongTamGiu());
        System.out.println("Khả dụng sau: " + (ton - spct.getSoLuongTamGiu()));

        ctRepo.save(hdct);
        spctRepo.save(spct);

        Integer idHoaDon = hdct.getIdHoaDon().getId();

        recalculateHoaDon(idHoaDon);
        kiemTraVoucherConHopLe(idHoaDon);

        HoaDon hoaDon = hdct.getIdHoaDon();
        hoaDon.setNgayCapNhat(LocalDateTime.now());
        hoaDonRepo.save(hoaDon);

        posSocketService.send(
                new PosEvent(
                        "QUANTITY_UPDATED",
                        idHoaDon,
                        spct.getId(),
                        ton - spct.getSoLuongTamGiu()
                )
        );

        System.out.println("========== KẾT THÚC ==========\n");
    }

    @Override
    public List<GioHangResponse> getChiTietHoaDon(Integer idHoaDon) {

        List<HoaDonChiTiet> ds = ctRepo.findByIdHoaDon_Id(idHoaDon);

        for (SanPhamGiamGia x : ds.get(0).getIdSanPhamChiTiet().getSanPhamGiamGias()) {
            System.out.println(
                    x.getDotGiamGia().getMaDotGiamGia()
            );
        }

        return ds.stream()
                .map(ct -> new GioHangResponse(

                        ct.getId(),

                        ct.getIdSanPhamChiTiet(), // ✅ truyền cả object

                        ct.getIdHoaDon().getMaHoaDon(),

                        ct.getIdSanPhamChiTiet().getTenSanPhamChiTiet(),

                        ct.getDonGia(),

                        ct.getSoLuong(),

                        ct.getThanhTien(),

                        ct.getIdHoaDon().getTongGiamGia(),

                        ct.getIdHoaDon().getPhiVanChuyen(),

                        ct.getIdHoaDon().getTongThanhToan()
                ))
                .toList();
    }


    @Override
    @Transactional
    public List<VoucherKhachHangResponse> ganKhachHang(Integer idHoaDon, Integer idKhachHang) {

        // ====== Gắn khách vào hóa đơn ======
        HoaDon hoaDon = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        KhachHang khachHang = khRepo.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        hoaDon.setIdKhachHang(khachHang);
        hoaDon.setTenNguoiNhan(khachHang.getHoTen());
        hoaDon.setSoDienThoaiNguoiNhan(khachHang.getSoDienThoai());
        hoaDon.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hoaDon);

        // ====== Lấy voucher của khách ======
        List<VoucherCuaKhachHang> list =
                voucherCuaKhachHangRepo.findByIdKhachHangAndTrangThai(
                        idKhachHang,
                        "CHUA_DUNG"
                );

        List<VoucherKhachHangResponse> result = new ArrayList<>();

        for (VoucherCuaKhachHang vckh : list) {

            KhoVoucher kho = khoVoucherRepository.findById(vckh.getIdKhoVoucher())
                    .orElse(null);

            if (kho == null) continue;

            VoucherKhachHangResponse dto = new VoucherKhachHangResponse();

            // id của kho voucher (để FE hiển thị)
            dto.setId(kho.getId());

            // id của bảng voucher_cua_khach_hang (để thanh toán)
            dto.setIdVoucherKhachHang(vckh.getId());

            dto.setTenVoucher(kho.getTenVoucher());
            dto.setMaCode(kho.getMaCode());
            dto.setLoaiGiamGia(kho.getLoaiGiamGia());
            dto.setGiaTriGiam(kho.getGiaTriGiam());
            dto.setGiaTriGiamToiDa(kho.getGiaTriGiamToiDa());
            dto.setDieuKienToiThieu(kho.getDieuKienToiThieu());
            dto.setSoLuongConLai(kho.getSoLuongConLai());
            dto.setSoXuDoi(kho.getSoXuDoi());
            dto.setNgayBatDau(kho.getNgayBatDau());
            dto.setNgayHetHan(kho.getNgayHetHan());
            dto.setMoTa(kho.getMoTa());
            dto.setTrangThai(kho.getTrangThai());

            result.add(dto);
        }

        return result;
    }
    @Override
    @Transactional
    public List<KhoVoucher> goKhachHang(Integer idHoaDon) {

        HoaDon hoaDon = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        hoaDon.setIdKhachHang(null);
        hoaDon.setTenNguoiNhan(null);
        hoaDon.setSoDienThoaiNguoiNhan(null);
        hoaDon.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hoaDon);

        return Collections.emptyList();
    }

    private void removeVoucherIfExists(Integer idHoaDon) {

        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElse(null);

        if (hdVoucher != null) {
            hoaDonVoucherRepo.delete(hdVoucher);
        }
    }

    @Override
    @Transactional
    public void recalculateHoaDon(Integer idHoaDon) {

        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow();

        List<HoaDonChiTiet> ds = ctRepo.findByIdHoaDon_Id(idHoaDon);

        BigDecimal tongTienHang = ds.stream()
                .map(HoaDonChiTiet::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tongGiamGia = BigDecimal.ZERO;

        HoaDonVoucher hdVoucher = hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                .orElse(null);

        if (hdVoucher != null) {

            // Voucher hệ thống
            if (hdVoucher.getIdVoucher() != null) {

                tongGiamGia = tinhTienGiam(
                        hdVoucher.getIdVoucher(),
                        tongTienHang
                );
            }

            // Voucher đổi xu
            else if (hdVoucher.getIdKhoVoucher() != null) {

                tongGiamGia = tinhTienGiam(
                        hdVoucher.getIdKhoVoucher(),
                        tongTienHang
                );
            }

            hdVoucher.setSoTienGiam(tongGiamGia);
            hoaDonVoucherRepo.save(hdVoucher);
        }

        hd.setTongTienHang(tongTienHang);
        hd.setTongGiamGia(tongGiamGia);

        BigDecimal phiShip = hd.getPhiVanChuyen() != null
                ? hd.getPhiVanChuyen()
                : BigDecimal.ZERO;

        BigDecimal tongThanhToan = tongTienHang
                .subtract(tongGiamGia)
                .add(phiShip);

        if (tongThanhToan.compareTo(BigDecimal.ZERO) < 0) {
            tongThanhToan = BigDecimal.ZERO;
        }

        hd.setTongThanhToan(tongThanhToan);

        hoaDonRepo.save(hd);
    }
    private BigDecimal tinhTienGiam(
            Voucher voucher,
            BigDecimal tongTienHang
    ) {
        if (tongTienHang.compareTo(
                voucher.getGiaTriDonHangToiThieu()) < 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal tongGiamGia;
        if ("tien_mat".equals(
                voucher.getLoaiGiamGia()
        )) {
            tongGiamGia = voucher.getGiaTriGiam();
        } else {
            tongGiamGia = tongTienHang.multiply(
                    voucher.getGiaTriGiam()
                            .divide(
                                    BigDecimal.valueOf(100),
                                    2,
                                    RoundingMode.HALF_UP
                            )
            );
            if (
                    voucher.getGiaTriGiamToiDa() != null
                            &&
                            tongGiamGia.compareTo(
                                    voucher.getGiaTriGiamToiDa()
                            ) > 0
            ) {
                tongGiamGia =
                        voucher.getGiaTriGiamToiDa();
            }
        }
        return tongGiamGia;
    }
    private BigDecimal tinhTienGiam(
            KhoVoucher voucher,
            BigDecimal tongTienHang
    ) {
        if (tongTienHang.compareTo(voucher.getDieuKienToiThieu()) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal tongGiamGia;

        if ("tien_mat".equals(voucher.getLoaiGiamGia())) {

            tongGiamGia = voucher.getGiaTriGiam();

        } else {

            tongGiamGia = tongTienHang.multiply(
                    voucher.getGiaTriGiam()
                            .divide(
                                    BigDecimal.valueOf(100),
                                    2,
                                    RoundingMode.HALF_UP
                            )
            );

            if (voucher.getGiaTriGiamToiDa() != null
                    && tongGiamGia.compareTo(voucher.getGiaTriGiamToiDa()) > 0) {

                tongGiamGia = voucher.getGiaTriGiamToiDa();
            }
        }

        return tongGiamGia;
    }
    @Override
    @Transactional
    public void apVoucher(Integer idHoaDon, Integer idVoucher, Integer idVoucherKhachHang) {

        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        Voucher voucherToUse = null;
        KhoVoucher khoVoucherToUse = null;
        VoucherCuaKhachHang voucherKhachToUse = null; // <-- thêm

        // ==============================
        // 1. Voucher hệ thống
        // ==============================
        if (idVoucher != null) {

            voucherToUse = voucherRepo.findById(idVoucher)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher hệ thống"));

            if (voucherToUse.getSoLuongDaDung() >= voucherToUse.getSoLuong()) {
                throw new RuntimeException("Voucher đã hết lượt sử dụng");
            }

            if (hd.getTongTienHang().compareTo(voucherToUse.getGiaTriDonHangToiThieu()) < 0) {
                throw new RuntimeException("Đơn hàng chưa đạt giá trị tối thiểu");
            }
        }

        // ==============================
        // 2. Voucher đổi xu
        // ==============================
        else if (idVoucherKhachHang != null) {

            voucherKhachToUse = voucherCuaKhachHangRepo
                    .findByIdAndTrangThai(idVoucherKhachHang, "CHUA_DUNG")
                    .orElseThrow(() ->
                            new RuntimeException("Voucher không tồn tại hoặc đã sử dụng"));

            khoVoucherToUse = khoVoucherRepository.findById(voucherKhachToUse.getIdKhoVoucher())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin kho voucher"));

            if (khoVoucherToUse.getSoLuongConLai() <= 0) {
                throw new RuntimeException("Voucher đã hết lượt sử dụng");
            }

            if (hd.getTongTienHang().compareTo(khoVoucherToUse.getDieuKienToiThieu()) < 0) {
                throw new RuntimeException("Đơn hàng chưa đạt giá trị tối thiểu");
            }
        }

        else {
            throw new RuntimeException("Vui lòng chọn voucher");
        }

        // ==============================
        // Tìm hoặc tạo mới HoaDonVoucher
        // ==============================
        HoaDonVoucher hdVoucher = hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                .orElse(new HoaDonVoucher());

        hdVoucher.setIdHoaDon(hd);

        BigDecimal tienGiam;

        // Voucher hệ thống
        if (voucherToUse != null) {

            hdVoucher.setIdVoucher(voucherToUse);
            hdVoucher.setIdKhoVoucher(null);
            hdVoucher.setVoucherCuaKhachHang(null);
            hdVoucher.setDaConsume(false);
            tienGiam = tinhTienGiam(voucherToUse, hd.getTongTienHang());
        }

        // Voucher đổi xu
        else {

            hdVoucher.setIdVoucher(null);
            hdVoucher.setIdKhoVoucher(khoVoucherToUse);
            hdVoucher.setVoucherCuaKhachHang(voucherKhachToUse);
            hdVoucher.setDaConsume(false);
            tienGiam = tinhTienGiam(khoVoucherToUse, hd.getTongTienHang());
        }

        hdVoucher.setSoTienGiam(tienGiam);

        hd.setNgayCapNhat(LocalDateTime.now());

        hoaDonVoucherRepo.save(hdVoucher);

        hd.setTongGiamGia(tienGiam);
        hoaDonRepo.save(hd);

        recalculateHoaDon(idHoaDon);
    }

    @Override
    @Transactional
    public void boVoucher(Integer idHoaDon) {

        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElse(null);

        if (hdVoucher == null) {
            return;
        }


        // Xóa liên kết voucher (dù là voucher hệ thống hay voucher đổi xu)
        hoaDonVoucherRepo.delete(hdVoucher);

        // Reset giảm giá
        hd.setTongGiamGia(BigDecimal.ZERO);
        hd.setNgayCapNhat(LocalDateTime.now());
        hoaDonRepo.save(hd);

        // Tính lại tổng tiền
        recalculateHoaDon(idHoaDon);
    }

    @Override
    public List<SanPhamTraResponse> getDanhSachTra(Integer hoaDonId) {

        List<HoaDonChiTiet> dsHDCT =
                ctRepo.findByIdHoaDon_Id(hoaDonId);

        List<SanPhamTraResponse> result = new ArrayList<>();

        for (HoaDonChiTiet hdct : dsHDCT) {

            Integer daTra = traHangChiTietRepository.tongDaTra(hdct.getId());

            Integer conLai = hdct.getSoLuong() - daTra;

            // nếu đã trả hết thì bỏ qua
            if (conLai <= 0) {
                continue;
            }

            SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

            SanPhamTraResponse response = new SanPhamTraResponse();

            // HDCT
            response.setHdctId(hdct.getId());

            // SPCT
            response.setSpctId(spct.getId());

            // Thông tin sản phẩm
            response.setTenSanPham(
                    spct.getIdSanPham().getTenSanPham());

            response.setThuongHieu(
                    spct.getIdSanPham().getIdThuongHieu().getTenThuongHieu());

            response.setMauSac(
                    spct.getIdMauSac().getTenMauSac());

            response.setKichThuoc(
                    spct.getIdKichThuoc().getTenKichThuoc());

            response.setChatLieu(
                    spct.getIdSanPham().getIdChatLieu().getTenChatLieu());

            // Giá lúc mua
            response.setDonGia(hdct.getDonGia());

            // Số lượng
            response.setSoLuongMua(hdct.getSoLuong());
            response.setDaTra(daTra);
            response.setConLai(conLai);

            // Thành tiền dòng hóa đơn
            response.setThanhTien(hdct.getThanhTien());
            response.setSoLuongTra(1);
            result.add(response);
        }

        return result;
    }
    private BigDecimal getGiaThucTeHienTai(SanPhamChiTiet spct) {
        BigDecimal giaGoc = spct.getGiaBan() != null ? spct.getGiaBan() : BigDecimal.ZERO;

        Optional<SanPhamGiamGia> spggOpt = sanPhamGiamGiaRepository
                .findActiveBySanPhamChiTietId(spct.getId(), Instant.now());

        if (spggOpt.isPresent()) {
            DotGiamGia dgg = spggOpt.get().getDotGiamGia();

            if (dgg != null && dgg.getGiaTriGiam() != null && dgg.getGiaTriGiam().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal soTienGiam = BigDecimal.ZERO;

                boolean isPhanTram = "%".equals(dgg.getLoaiGiamGia())
                        || "PERCENT".equalsIgnoreCase(dgg.getLoaiGiamGia());

                if (isPhanTram) {
                    soTienGiam = giaGoc.multiply(dgg.getGiaTriGiam())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                    if (dgg.getGiaTriGiamToiDa() != null && dgg.getGiaTriGiamToiDa().compareTo(BigDecimal.ZERO) > 0) {
                        soTienGiam = soTienGiam.min(dgg.getGiaTriGiamToiDa());
                    }
                } else {
                    soTienGiam = dgg.getGiaTriGiam();
                }

                return giaGoc.subtract(soTienGiam).max(BigDecimal.ZERO);
            }
        }

        return giaGoc;
    }
    @Transactional
    public Map<String, Object> createOnlineOrder(CreateOnlineOrderRequest req, HttpServletRequest request) {

        // =========================================================================
        // 🔴 BƯỚC 0: VALIDATE TOÀN BỘ GIỎ HÀNG TRƯỚC KHI TẠO ĐƠN
        // =========================================================================
        if (req.getItems() == null || req.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng của bạn đang trống!");
        }

        for (CreateOnlineOrderRequest.Item item : req.getItems()) {
            // 1. Tìm sản phẩm chi tiết
            SanPhamChiTiet spct = spctRepo.findById(item.getProductDetailId())
                    .orElseThrow(() -> new RuntimeException("Có sản phẩm trong giỏ không tồn tại hoặc đã bị xóa!"));

            SanPham sp = spct.getIdSanPham();
            String tenSp = spct.getTenSanPhamChiTiet() != null ? spct.getTenSanPhamChiTiet() : sp.getTenSanPham();

            // 2. Validate: Trạng thái kinh doanh của SP Cha và SPCT
            boolean isSpChaDangKinhDoanh = Boolean.TRUE.equals(sp.getTrangThai())
                    || "true".equalsIgnoreCase(String.valueOf(sp.getTrangThai()).trim());
            boolean isSpctDangKinhDoanh = Boolean.TRUE.equals(spct.getTrangThai())
                    || "true".equalsIgnoreCase(String.valueOf(spct.getTrangThai()).trim());

            if (!isSpChaDangKinhDoanh || !isSpctDangKinhDoanh) {
                throw new RuntimeException("Sản phẩm '" + tenSp + "' đã ngừng kinh doanh. Vui lòng xóa khỏi giỏ hàng!");
            }

            // 3. Validate: Số lượng khả dụng (Tồn kho - Tạm giữ)
            int soLuongTon = spct.getSoLuongTon() != null ? spct.getSoLuongTon() : 0;
            int soLuongTamGiu = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;
            int soLuongKhaDung = soLuongTon - soLuongTamGiu;

            if (item.getQuantity() > soLuongKhaDung) {
                if (soLuongKhaDung <= 0) {
                    throw new RuntimeException("Sản phẩm '" + tenSp + "' hiện đã HẾT HÀNG!");
                } else {
                    throw new RuntimeException("Sản phẩm '" + tenSp + "' chỉ còn lại " + soLuongKhaDung + " sản phẩm khả dụng!");
                }
            }

            // 🟢 4. Validate: Biến động giá (Dùng giá thực tế đã tính đợt giảm giá)
            if (item.getPrice() != null) {
                BigDecimal giaThucTeBackend = getGiaThucTeHienTai(spct);

                if (giaThucTeBackend.compareTo(item.getPrice()) != 0) {
                    throw new RuntimeException("Giá của sản phẩm '" + tenSp + "' đã thay đổi. Vui lòng tải lại trang để cập nhật giá mới!");
                }
            }
        }

        // =========================================================================
        // 🟢 BƯỚC 1: CÁC VALIDATE ĐÃ PASS -> TIẾN HÀNH TẠO HÓA ĐƠN ONLINE
        // =========================================================================
        HoaDon hd = taoHoaDonChoOnline();

        // 2. Xử lý khách hàng
        String auth = request.getHeader("Authorization");

        if (auth != null && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);
            Integer idTaiKhoan = jwtService.extractId(token);

            KhachHang khachHang = khachHangRepository
                    .findByIdTaiKhoan_Id(idTaiKhoan)
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy khách hàng"));

            ganKhachHang(hd.getId(), khachHang.getId());

            DiaChiKhachHang diaChi;

            if (req.getAddressId() != null) {
                diaChi = diaChiKhachHangRepository
                        .findById(req.getAddressId())
                        .filter(dc ->
                                dc.getIdKhachHang()
                                        .getId()
                                        .equals(khachHang.getId()))
                        .orElseThrow(() ->
                                new RuntimeException("Địa chỉ không hợp lệ"));
            } else {
                diaChi = diaChiKhachHangRepository
                        .findByIdKhachHang_IdAndMacDinhTrue(
                                khachHang.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Khách hàng chưa có địa chỉ mặc định"
                                ));
            }

            hd.setDiaChiGiaoHang(
                    diaChi.getDiaChiCuThe()
                            + ", "
                            + diaChi.getPhuong()
                            + ", "
                            + diaChi.getQuan()
                            + ", "
                            + diaChi.getThanhPho()
            );
        } else {
            if (req.getTenNguoiNhan() == null || req.getTenNguoiNhan().isBlank()) {
                throw new RuntimeException("Thiếu tên người nhận");
            }

            if (req.getSoDienThoaiNguoiNhan() == null || !req.getSoDienThoaiNguoiNhan().matches("^[0-9]{9,11}$")) {
                throw new RuntimeException("Số điện thoại người nhận không hợp lệ");
            }

            if (req.getThanhPho() == null || req.getQuan() == null || req.getPhuong() == null
                    || req.getDiaChiCuThe() == null || req.getDiaChiCuThe().isBlank()) {
                throw new RuntimeException("Thiếu địa chỉ giao hàng");
            }

            hd.setTenNguoiNhan(req.getTenNguoiNhan().trim());
            hd.setSoDienThoaiNguoiNhan(req.getSoDienThoaiNguoiNhan().trim());

            hd.setDiaChiGiaoHang(
                    req.getDiaChiCuThe().trim()
                            + ", "
                            + req.getPhuong()
                            + ", "
                            + req.getQuan()
                            + ", "
                            + req.getThanhPho()
            );
        }

        // 3. Thêm toàn bộ sản phẩm vào hóa đơn
        for (CreateOnlineOrderRequest.Item item : req.getItems()) {
            ThemSanPhamRequest them = new ThemSanPhamRequest();
            them.setIdHoaDon(hd.getId());
            them.setIdSanPhamChiTiet(item.getProductDetailId());
            them.setSoLuong(item.getQuantity());

            // 📌 Nếu ThemSanPhamRequest có trường giá, bạn set thêm ở đây:
            // them.setDonGia(getGiaThucTeHienTai(spctRepo.findById(item.getProductDetailId()).get()));

            themSanPhamVaoHoaDon(them);
        }



// 4. Áp voucher (hệ thống hoặc voucher khách hàng)
        // 4. Áp voucher (hệ thống hoặc voucher khách)
        if (req.getVoucherId() != null || req.getVoucherKhachHangId() != null) {

            apVoucher(
                    hd.getId(),
                    req.getVoucherId(),
                    req.getVoucherKhachHangId()
            );
        }

        // 5. Load lại hóa đơn
        hd = hoaDonRepo.findById(hd.getId()).orElseThrow();

        // 6. Gán phí ship & Ghi chú
        hd.setPhiVanChuyen(req.getShippingFee());
        hd.setGhiChu(req.getNote());

        hoaDonRepo.save(hd);

        // 8. Tính toán lại tổng tiền
        recalculateHoaDon(hd.getId());

        hd = hoaDonRepo.findById(hd.getId()).orElseThrow();

        String qrUrl = VietQrUtil.createQrUrl(
                hd.getTongThanhToan().longValue(),
                hd.getMaHoaDon()
        );

        // 9. Đóng gói kết quả trả về
        Map<String, Object> map = new HashMap<>();
        map.put("id", hd.getId());
        map.put("maHoaDon", hd.getMaHoaDon());
        map.put("qrUrl", qrUrl);

        return map;
    }

    @Override
    @Transactional
    public void huyHoaDonOnline(Integer idHoaDon) {

        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        // Chỉ cho phép khách hủy khi đơn đang chờ xác nhận
        if (!"cho_xac_nhan".equals(hd.getTrangThai())) {
            throw new RuntimeException("Hóa đơn không thể hủy");
        }

        List<HoaDonChiTiet> dsChiTiet = ctRepo.findByIdHoaDon_Id(idHoaDon);

        // Giải phóng số lượng tạm giữ
        for (HoaDonChiTiet ct : dsChiTiet) {

            SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();

            int tamGiuMoi = spct.getSoLuongTamGiu() - ct.getSoLuong();

            if (tamGiuMoi < 0) {
                tamGiuMoi = 0;
            }

            spct.setSoLuongTamGiu(tamGiuMoi);

            spctRepo.save(spct);
        }

        // Hoàn lại lượt dùng voucher (nếu có)
        HoaDonVoucher hdVoucher = hoaDonVoucherRepo
                .findByIdHoaDon_Id(idHoaDon)
                .orElse(null);

        if (hdVoucher != null) {

            Voucher voucher = hdVoucher.getIdVoucher();

            if (voucher != null && voucher.getSoLuongDaDung() > 0) {
                voucher.setSoLuongDaDung(voucher.getSoLuongDaDung() - 1);
                voucherRepo.save(voucher);
            }

            hoaDonVoucherRepo.delete(hdVoucher);
        }

        // Cập nhật trạng thái hóa đơn
        hd.setTrangThai("da_huy");
        hd.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hd);

        // Thông báo realtime
        messagingTemplate.convertAndSend(
                "/topic/products",
                "STOCK_CHANGED"
        );
    }


    @Override
    @Transactional
    public void updateTrangThai(Integer id, String trangThaiMoi) {

        // 1. Tìm hóa đơn theo ID
        HoaDon hoaDon = hoaDonRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        // 2. Chuyển đổi và kiểm tra trạng thái mới
        TrangThaiHoaDon newStatus;
        try {
            newStatus = TrangThaiHoaDon.fromValue(trangThaiMoi);
        } catch (Exception e) {
            throw new RuntimeException("Trạng thái mới không hợp lệ");
        }

        // 3. Chuyển đổi và kiểm tra trạng thái hiện tại
        TrangThaiHoaDon currentStatus;
        try {
            currentStatus = TrangThaiHoaDon.fromValue(hoaDon.getTrangThai());
        } catch (Exception e) {
            throw new RuntimeException("Trạng thái hiện tại của hóa đơn không hợp lệ");
        }

        // 4. Kiểm tra nếu đơn hàng đã bị hủy
        if (currentStatus == TrangThaiHoaDon.DA_HUY) {
            throw new RuntimeException("Đơn đã hủy không thể cập nhật trạng thái khác");
        }

        // 5. Kiểm tra quy tắc chuyển trạng thái (State machine rule)
        if (!TrangThaiRule.ALLOWED.containsKey(currentStatus) ||
                !TrangThaiRule.ALLOWED.get(currentStatus).contains(newStatus)) {

            throw new RuntimeException(
                    "Không thể chuyển trạng thái từ '"
                            + currentStatus.getValue()
                            + "' sang '"
                            + newStatus.getValue() + "'"
            );
        }

        // Lấy chi tiết hóa đơn
        List<HoaDonChiTiet> dsChiTiet = ctRepo.findByIdHoaDon_Id(id);


        System.out.println("Current: " + currentStatus);
        System.out.println("New: " + newStatus);
        if (newStatus == TrangThaiHoaDon.DA_HUY) {

            System.out.println("===== VAO KHOI HUY =====");


            for (HoaDonChiTiet ct : dsChiTiet) {

                SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();

                int tamGiuMoi = spct.getSoLuongTamGiu() - ct.getSoLuong();

                if (tamGiuMoi < 0) {
                    tamGiuMoi = 0;
                }

                spct.setSoLuongTamGiu(tamGiuMoi);

                spctRepo.save(spct);
            }

            // Nếu đã dùng voucher thì trả lại lượt dùng
            HoaDonVoucher hdVoucher = hoaDonVoucherRepo
                    .findByIdHoaDon_Id(id)
                    .orElse(null);

            if (hdVoucher != null) {

                // Chỉ hoàn nếu voucher đã consume
                if (Boolean.TRUE.equals(hdVoucher.getDaConsume())) {

                    // Voucher hệ thống
                    if (hdVoucher.getIdVoucher() != null) {

                        Voucher voucher = hdVoucher.getIdVoucher();

                        voucher.setSoLuongDaDung(
                                Math.max(0,
                                        voucher.getSoLuongDaDung() == null
                                                ? 0
                                                : voucher.getSoLuongDaDung() - 1)
                        );
                        voucher.setSoLuong(voucher.getSoLuong() + 1);

                        voucherRepo.save(voucher);
                    }

                    // Voucher khách
                    if (hdVoucher.getVoucherCuaKhachHang() != null) {

                        VoucherCuaKhachHang voucherKhach =
                                hdVoucher.getVoucherCuaKhachHang();

                        voucherKhach.setTrangThai("CHUA_DUNG");
                        voucherCuaKhachHangRepo.save(voucherKhach);

                    }
                }

                // Xóa bản ghi hóa đơn voucher
                hoaDonVoucherRepo.delete(hdVoucher);
            }
        }
        if (newStatus == TrangThaiHoaDon.GIAO_THANH_CONG) {

            for (HoaDonChiTiet ct : dsChiTiet) {

                SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();

                int tonMoi = spct.getSoLuongTon() - ct.getSoLuong();

                if (tonMoi < 0) {
                    throw new RuntimeException(
                            "Sản phẩm "
                                    + spct.getMaSanPhamChiTiet()
                                    + " không đủ tồn kho."
                    );
                }

                int tamGiuMoi = spct.getSoLuongTamGiu() - ct.getSoLuong();

                if (tamGiuMoi < 0) {
                    tamGiuMoi = 0;
                }

                spct.setSoLuongTon(tonMoi);
                spct.setSoLuongTamGiu(tamGiuMoi);

                spctRepo.save(spct);
            }

            // COD: giao thành công mới thu tiền
            if ("chua_thanh_toan".equals(hoaDon.getTrangThaiThanhToan())) {

                hoaDon.setTrangThaiThanhToan("da_thanh_toan");

                ThanhToan thanhToan = ttRepo.findByIdHoaDon_Id(id);

                if (thanhToan != null) {
                    thanhToan.setTrangThai("da_thanh_toan");
                    thanhToan.setNgayThanhToan(LocalDateTime.now());
                    ttRepo.save(thanhToan);
                }
            }
        }

        // 6. Cập nhật trạng thái mới và thời gian cập nhật vào Entity
        hoaDon.setTrangThai(newStatus.getValue());
        hoaDon.setNgayCapNhat(LocalDateTime.now());


        // 7. Lưu thay đổi xuống Database
        hoaDonRepo.save(hoaDon);
    }

    private void kiemTraVoucherConHopLe(Integer idHoaDon) {

        HoaDonVoucher hdVoucher = hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                .orElse(null);

        if (hdVoucher == null) {
            return;
        }

        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        LocalDateTime now = LocalDateTime.now();

        // ==========================================================
        // 1. Voucher hệ thống
        // ==========================================================
        if (hdVoucher.getIdVoucher() != null) {

            Voucher voucher = hdVoucher.getIdVoucher();

            boolean khongHopLe =
                    voucher.getSoLuongDaDung() >= voucher.getSoLuong()
                            || voucher.getTrangThai() == 0
                            || voucher.getNgayBatDau().isAfter(now)
                            || voucher.getNgayKetThuc().isBefore(now)
                            || hd.getTongTienHang().compareTo(voucher.getGiaTriDonHangToiThieu()) < 0;

            if (khongHopLe) {

                hoaDonVoucherRepo.delete(hdVoucher);

                posSocketService.send(
                        new PosEvent(
                                "VOUCHER_REMOVED",
                                idHoaDon,
                                voucher.getId(),
                                null
                        )
                );
            }

            return;
        }

        // ==========================================================
        // 2. Voucher khách hàng (KhoVoucher)
        // ==========================================================
        if (hdVoucher.getIdKhoVoucher() != null) {

            KhoVoucher voucher = hdVoucher.getIdKhoVoucher();

            boolean khongHopLe =
                    voucher.getSoLuongConLai() <= 0
                            || !Boolean.TRUE.equals(voucher.getTrangThai())
                            || voucher.getNgayBatDau().isAfter(now)
                            || voucher.getNgayHetHan().isBefore(now)
                            || hd.getTongTienHang().compareTo(voucher.getDieuKienToiThieu()) < 0;

            if (khongHopLe) {

                hoaDonVoucherRepo.delete(hdVoucher);

                posSocketService.send(
                        new PosEvent(
                                "VOUCHER_REMOVED",
                                idHoaDon,
                                null,
                                voucher.getId()
                        )
                );
            }
        }
    }

    @Override
    public Map<String, Object> taoQr(Integer hoaDonId, Long amount) {
        HoaDon hd = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        // Nếu có truyền amount (dùng cho thanh toán kết hợp), lấy amount đó; không thì lấy tổng tiền hóa đơn
        Long finalAmount = (amount != null) ? amount : hd.getTongThanhToan().longValue();

        String qrUrl = VietQrUtil.createQrUrl(
                finalAmount,
                hd.getMaHoaDon()
        );

        Map<String, Object> result = new HashMap<>();
        result.put("id", hd.getId());
        result.put("maHoaDon", hd.getMaHoaDon());
        result.put("tongTien", finalAmount);
        result.put("qrUrl", qrUrl);

        return result;
    }


    @Override
    @Transactional
    public void capNhatHoaDonTheoVoucher(Integer idVoucher) {

        List<HoaDonVoucher> list =
                hoaDonVoucherRepo.findByIdVoucher_Id(idVoucher);


        for (HoaDonVoucher hdv : list) {


            Integer idHoaDon =
                    hdv.getIdHoaDon().getId();


            // kiểm tra voucher còn hợp lệ không
            kiemTraVoucherConHopLe(idHoaDon);


            // tính lại tiền
            recalculateHoaDon(idHoaDon);

        }
    }
    @Override
    @Transactional
    public void capNhatHoaDonTheoKhoVoucher(Integer idKhoVoucher) {

        List<HoaDonVoucher> list =
                hoaDonVoucherRepo.findByIdKhoVoucher_Id(idKhoVoucher);

        for (HoaDonVoucher hdv : list) {

            Integer idHoaDon = hdv.getIdHoaDon().getId();

            // kiểm tra voucher còn hợp lệ
            kiemTraVoucherConHopLe(idHoaDon);

            // tính lại tiền
            recalculateHoaDon(idHoaDon);
        }
    }


}
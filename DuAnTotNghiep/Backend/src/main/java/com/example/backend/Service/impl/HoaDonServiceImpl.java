package com.example.backend.Service.impl;

import com.example.backend.Config.WebSocketConfig;
import com.example.backend.Entity.*;

import com.example.backend.Repository.*;
import com.example.backend.Request.*;
import com.example.backend.Response.*;
import com.example.backend.Service.HoaDonService;
import com.example.backend.mapper.HoaDonMapper;
import com.example.backend.secutity.JwtService;
import com.example.backend.specification.HoaDonSpecification;
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
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private HinhAnhRepository hinhAnhRepository;
    private final JwtService jwtService;
    private final KhachHangRepository khachHangRepository;

    @Override
    public HoaDon taoHoaDonCho() {
        HoaDon hoaDon = new HoaDon();
        long count = hoaDonRepo.count() + 1;
        hoaDon.setMaHoaDon(String.format("HD%02d", count));
        hoaDon.setLoaiHoaDon("tai_quay");
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

        return hoaDonRepo.save(hoaDon);
    }


    @Override
    public List<HoaDon> getHoaDonCho() {
        return hoaDonRepo.findByTrangThaiOrderByNgayTaoDesc(
                "cho_xac_nhan"
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

        List<HoaDonChiTietResponse> sanPhams =
                ctRepo.findByHoaDonWithAnh(id)
                        .stream()
                        .map(item -> {

                            HoaDonChiTiet ct = item.getHoaDonChiTiet();
                            SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();

                            String anh = item.getAnh() != null
                                    ? "/sanpham/" + item.getAnh()
                                    : null;

                            return new HoaDonChiTietResponse(
                                    ct.getId(),
                                    spct.getId(),
                                    spct.getMaSanPhamChiTiet(),
                                    spct.getIdSanPham().getTenSanPham(),
                                    spct.getIdMauSac().getTenMauSac(),
                                    spct.getIdKichThuoc().getTenKichThuoc(),
                                    spct.getIdSanPham()
                                            .getIdThuongHieu()
                                            .getTenThuongHieu(),
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
    @Transactional
    public Object thanhToanHoaDon(
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

        tt.setTrangThai(
                "da_thanh_toan");

        tt.setNgayThanhToan(
                Instant.now());

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
        hd.setTrangThai(
                "hoan_thanh");

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
            sp.put("maSanPhamChiTiet",ct.getIdSanPhamChiTiet().getMaSanPhamChiTiet());
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

        tt.setTrangThai(
                "da_thanh_toan");

        tt.setNgayThanhToan(
                Instant.now());

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
        hd.setTrangThai(
                "hoan_thanh");

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
            sp.put("maSanPhamChiTiet",ct.getIdSanPhamChiTiet().getMaSanPhamChiTiet());
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

        // kiểm tra tồn kho
        if (spct.getSoLuongTon() < request.getSoLuong()) {
            throw new RuntimeException(
                    "Sản phẩm không đủ tồn kho"
            );
        }

        Optional<HoaDonChiTiet> hdctOpt =
                ctRepo.findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(
                        hoaDon.getId(),
                        spct.getId()
                );

        // nếu đã có trong hóa đơn
        if (hdctOpt.isPresent()) {
            HoaDonChiTiet hdct =
                    hdctOpt.get();
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
        } else {
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
            hdct.setDonGia(
                    spct.getGiaBan()
            );
            hdct.setThanhTien(
                    spct.getGiaBan().multiply(
                            BigDecimal.valueOf(
                                    request.getSoLuong()
                            )
                    )
            );
            ctRepo.save(hdct);
        }
        // trừ kho
        spct.setSoLuongTon(
                spct.getSoLuongTon()
                        - request.getSoLuong()
        );
        spctRepo.save(spct);
        recalculateHoaDon(
                hoaDon.getId()
        );
        messagingTemplate.convertAndSend(
                "/topic/products",
                "STOCK_CHANGED"
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
        // trả kho
        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

        spct.setSoLuongTon(
                spct.getSoLuongTon() + 1
        );
        spctRepo.save(spct);
        recalculateHoaDon(hdct.getIdHoaDon().getId());
    }


    @Override
    public List<GioHangResponse> getChiTietHoaDon(Integer idHoaDon) {

        List<HoaDonChiTiet> ds = ctRepo.findByIdHoaDon_Id(idHoaDon);

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

    @Transactional
    @Override
    public void xoaSanPhamKhoiHoaDon(
            Integer idHoaDonChiTiet
    ) {

        HoaDonChiTiet hdct =
                ctRepo.findById(idHoaDonChiTiet)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy sản phẩm"));

        // trả kho
        SanPhamChiTiet spct =
                hdct.getIdSanPhamChiTiet();

        spct.setSoLuongTon(
                spct.getSoLuongTon()
                        + hdct.getSoLuong()
        );

        spctRepo.save(spct);

        Integer idHoaDon =
                hdct.getIdHoaDon().getId();

        ctRepo.delete(hdct);

        recalculateHoaDon(idHoaDon);
    }

    @Transactional
    @Override
    public void tangSoLuong(Integer idHoaDonChiTiet) {

        HoaDonChiTiet hdct = ctRepo.findById(idHoaDonChiTiet)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

        if (spct.getSoLuongTon() <= 0) {
            throw new RuntimeException("Sản phẩm đã hết hàng");
        }

        hdct.setSoLuong(hdct.getSoLuong() + 1);

        hdct.setThanhTien(
                hdct.getDonGia()
                        .multiply(BigDecimal.valueOf(hdct.getSoLuong()))
        );

        ctRepo.save(hdct);

        // trừ kho
        spct.setSoLuongTon(spct.getSoLuongTon() - 1);
        spctRepo.save(spct);

        recalculateHoaDon(hdct.getIdHoaDon().getId());
    }

    @Override
    @Transactional
    public void ganKhachHang(Integer idHoaDon, Integer idKhachHang) {

        HoaDon hoaDon = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        KhachHang khachHang = khRepo.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        hoaDon.setIdKhachHang(khachHang);

        hoaDon.setTenNguoiNhan(
                khachHang.getHoTen()
        );

        hoaDon.setSoDienThoaiNguoiNhan(
                khachHang.getSoDienThoai()
        );

        hoaDonRepo.save(hoaDon);
    }

    @Transactional
    @Override
    public void huyHoaDon(Integer idHoaDon) {
        // 1. Lấy hóa đơn
        HoaDon hoaDon = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));
        // 2. Chặn nếu đã thanh toán
        if (hoaDon.getTrangThai().equalsIgnoreCase("hoan_thanh")) {
            throw new RuntimeException("Không thể hủy hóa đơn đã thanh toán");
        }
        // 3. Lấy danh sách chi tiết hóa đơn
        List<HoaDonChiTiet> listCT =
                ctRepo.findByIdHoaDon_Id(idHoaDon);
        // 4. HOÀN KHO (QUAN TRỌNG NHẤT)
        for (HoaDonChiTiet hdct : listCT) {

            SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

            spct.setSoLuongTon(
                    spct.getSoLuongTon() + hdct.getSoLuong()
            );

            spctRepo.save(spct);
        }

        // 5. KHÔNG XÓA HDCT (giữ lịch sử)
        // 6. CẬP NHẬT HÓA ĐƠN
        hoaDon.setTrangThai("da_huy");

        // optional: clear dữ liệu nghiệp vụ (không bắt buộc)
        hoaDon.setTongThanhToan(BigDecimal.ZERO);
        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElse(null);
        if (hdVoucher != null) {
            hoaDonVoucherRepo.delete(hdVoucher);
        }
        hoaDonRepo.save(hoaDon);
    }

    @Override
    @Transactional
    public void recalculateHoaDon(
            Integer idHoaDon
    ) {

        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow();
        List<HoaDonChiTiet> ds =
                ctRepo.findByIdHoaDon_Id(idHoaDon);
        BigDecimal tongTienHang =
                ds.stream()
                        .map(HoaDonChiTiet::getThanhTien)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );
        BigDecimal tongGiamGia =
                BigDecimal.ZERO;
        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElse(null);
        if (hdVoucher != null) {
            tongGiamGia =
                    tinhTienGiam(
                            hdVoucher.getIdVoucher(),
                            tongTienHang
                    );
            hdVoucher.setSoTienGiam(
                    tongGiamGia
            );
            hoaDonVoucherRepo.save(
                    hdVoucher
            );
        }
        hd.setTongTienHang(
                tongTienHang
        );
        hd.setTongGiamGia(
                tongGiamGia
        );
        BigDecimal phiShip = hd.getPhiVanChuyen();
        if (phiShip == null) {
            phiShip = BigDecimal.ZERO;
        }

        BigDecimal tongThanhToan =
                tongTienHang
                        .subtract(tongGiamGia)
                        .add(phiShip);


        if (tongThanhToan.compareTo(BigDecimal.ZERO) < 0) {
            tongThanhToan = BigDecimal.ZERO;
        }

        hd.setTongThanhToan(tongThanhToan);
        hoaDonRepo.save(
                hd
        );
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

    @Override
    @Transactional
    public void apVoucher(
            Integer idHoaDon,
            Integer idVoucher
    ) {
        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hóa đơn"));
        Voucher voucher = voucherRepo.findById(idVoucher)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy voucher"));
        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElse(new HoaDonVoucher());
        if (voucher.getSoLuongDaDung() >= voucher.getSoLuong()) {
            throw new RuntimeException(
                    "Voucher đã hết lượt sử dụng");
        }
        if (hd.getTongTienHang().compareTo(
                voucher.getGiaTriDonHangToiThieu()) < 0) {

            throw new RuntimeException(
                    "Đơn hàng chưa đạt giá trị tối thiểu");
        }
        hdVoucher.setIdHoaDon(hd);
        hdVoucher.setIdVoucher(voucher);
        BigDecimal tienGiam = tinhTienGiam(voucher, hd.getTongTienHang());
        hdVoucher.setSoTienGiam(tienGiam);

        hoaDonVoucherRepo.save(hdVoucher);
        recalculateHoaDon(idHoaDon);
    }

    @Override
    @Transactional
    public void boVoucher(
            Integer idHoaDon
    ) {
        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElseThrow(() ->
                                new RuntimeException("Hóa đơn chưa áp voucher"));
        hoaDonVoucherRepo.delete(hdVoucher);
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

    @Transactional
    public Map<String, Object> createOnlineOrder(CreateOnlineOrderRequest req, HttpServletRequest request) {

        // 1. tạo hóa đơn online
        HoaDon hd = taoHoaDonChoOnline();

        // 2. khách hàng
        String auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("Thiếu token");
        }

        String token = auth.substring(7);

        Integer idTaiKhoan = jwtService.extractId(token);

        KhachHang khachHang = khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        ganKhachHang(
                hd.getId(),
                khachHang.getId()
        );

        // 3. thêm toàn bộ sản phẩm
        for (CreateOnlineOrderRequest.Item item : req.getItems()) {

            ThemSanPhamRequest them = new ThemSanPhamRequest();

            them.setIdHoaDon(
                    hd.getId()
            );

            them.setIdSanPhamChiTiet(
                    item.getProductDetailId()
            );

            them.setSoLuong(
                    item.getQuantity()
            );

            themSanPhamVaoHoaDon(them);
        }

        // 4. áp voucher
        if (req.getVoucherId() != null) {

            apVoucher(
                    hd.getId(),
                    req.getVoucherId()
            );
        }

        // 5. load lại
        hd = hoaDonRepo.findById(hd.getId()).orElseThrow();

        // 6. ship
        hd.setPhiVanChuyen(req.getShippingFee());

        // 7. ghi chú
        hd.setGhiChu(req.getNote());

        // TODO:
        // set địa chỉ nếu có bảng địa chỉ

        hoaDonRepo.save(hd);

        // 8. tính lại
        recalculateHoaDon(
                hd.getId()
        );

        // 9. trả về
        Map<String, Object> map = new HashMap<>();

        map.put(
                "id",
                hd.getId()
        );

        map.put(
                "maHoaDon",
                hd.getMaHoaDon()
        );

        return map;
    }

    @Override
    @Transactional
    public void huyHoaDonOnline(Integer idHoaDon) {

        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        // Chỉ hủy khi chưa thanh toán
        if (!"cho_xac_nhan".equals(hd.getTrangThai())) {
            throw new RuntimeException("Hóa đơn không thể hủy");
        }

        List<HoaDonChiTiet> dsChiTiet =
                ctRepo.findByIdHoaDon_Id(idHoaDon);

        // Hoàn lại tồn kho
        for (HoaDonChiTiet ct : dsChiTiet) {

            SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();

            spct.setSoLuongTon(
                    spct.getSoLuongTon() + ct.getSoLuong()
            );

            spctRepo.save(spct);
        }

        // Nếu đã áp voucher thì trả lại lượt sử dụng (nếu có logic giữ trước)
        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElse(null);

        if (hdVoucher != null) {
            hoaDonVoucherRepo.delete(hdVoucher);
        }

        hd.setTrangThai("da_huy");
        hd.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hd);

        // Cập nhật realtime nếu POS/Web đang mở
        messagingTemplate.convertAndSend(
                "/topic/products",
                "STOCK_CHANGED"
        );
    }

}
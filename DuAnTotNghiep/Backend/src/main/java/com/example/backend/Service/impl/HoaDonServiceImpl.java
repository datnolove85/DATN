package com.example.backend.Service.impl;

import com.example.backend.Config.WebSocketConfig;
import com.example.backend.Entity.*;
import com.example.backend.Service.PosSocketService;
import com.example.backend.Service.TrangThaiRule;
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

            Voucher voucher = hdVoucher.getIdVoucher();

            voucher.setSoLuongDaDung(
                    Optional.ofNullable(voucher.getSoLuongDaDung()).orElse(0) + 1
            );

            voucher.setSoLuong(voucher.getSoLuong() - 1);

            voucherRepo.save(voucher);

            posSocketService.send(
                    new PosEvent(
                            "VOUCHER_UPDATED",
                            null,
                            voucher.getId(),
                            null
                    )
            );
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
                "cho_xac_nhan");

        if (tt.getIdPhuongThucThanhToan().getMaPhuongThuc().equals("COD")) {
            hd.setTrangThaiThanhToan("chua_thanh_toan");
        } else {
            hd.setTrangThaiThanhToan("da_thanh_toan");
        }
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



        // Kiểm tra tồn kho
        if (spct.getSoLuongTon() < request.getSoLuong()) {

            throw new RuntimeException(
                    "Sản phẩm không đủ tồn kho"
            );

        }



    /*
        Không tìm theo giá nữa.

        Trước đây:
        SP + Giá
        => giá thay đổi sẽ không tìm thấy
        => tạo HoaDonChiTiet mới

        Hiện tại:
        Chỉ tìm:
        Hóa đơn + Sản phẩm

        => giá thay đổi sẽ update dòng cũ
    */
        Optional<HoaDonChiTiet> hdctOpt =
                ctRepo.findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(
                        hoaDon.getId(),
                        spct.getId()
                );



        // Sản phẩm đã tồn tại trong hóa đơn
        if (hdctOpt.isPresent()) {


            HoaDonChiTiet hdct =
                    hdctOpt.get();



            // cập nhật giá mới nhất
            // ví dụ:
            // đang giảm 200
            // admin đóng giảm giá
            // giá mới 300
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



        // Trừ kho
        spct.setSoLuongTon(
                spct.getSoLuongTon()
                        - request.getSoLuong()
        );


        spctRepo.save(spct);



        recalculateHoaDon(
                hoaDon.getId()
        );


        kiemTraVoucherConHopLe(
                hoaDon.getId()
        );



        posSocketService.send(
                new PosEvent(
                        "PRODUCT_UPDATED",
                        hoaDon.getId(),
                        spct.getId(),
                        spct.getSoLuongTon()
                )
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

        // Trả tồn kho
        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

        spct.setSoLuongTon(
                spct.getSoLuongTon() + 1
        );

        spctRepo.save(spct);
        HoaDon hoaDon = hdct.getIdHoaDon();

        hoaDon.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hoaDon);


        recalculateHoaDon(hdct.getIdHoaDon().getId());

        kiemTraVoucherConHopLe(hoaDon.getId());

        // ===== Gửi WebSocket =====
        posSocketService.send(
                new PosEvent(
                        "PRODUCT_UPDATED",
                        hdct.getIdHoaDon().getId(),
                        spct.getId(),
                        spct.getSoLuongTon()
                )
        );
    }


    @Override
    public List<GioHangResponse> getChiTietHoaDon(Integer idHoaDon) {


        List<HoaDonChiTiet> ds = ctRepo.findByIdHoaDon_Id(idHoaDon);

        System.out.println("==========");
        System.out.println(
                ds.get(0)
                        .getIdSanPhamChiTiet()
                        .getSanPhamGiamGias()
                        .size()
        );

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

    @Transactional
    @Override
    public void xoaSanPhamKhoiHoaDon(Integer idHoaDonChiTiet) {

        HoaDonChiTiet hdct =
                ctRepo.findById(idHoaDonChiTiet)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy sản phẩm"));

        // Trả tồn kho
        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

        spct.setSoLuongTon(
                spct.getSoLuongTon() + hdct.getSoLuong()
        );

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
                        "PRODUCT_UPDATED",
                        idHoaDon,
                        spct.getId(),
                        spct.getSoLuongTon()
                )
        );
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

        // Tăng số lượng trong hóa đơn
        hdct.setSoLuong(hdct.getSoLuong() + 1);

        hdct.setThanhTien(
                hdct.getDonGia()
                        .multiply(BigDecimal.valueOf(hdct.getSoLuong()))
        );

        ctRepo.save(hdct);

        // Trừ tồn kho
        spct.setSoLuongTon(spct.getSoLuongTon() - 1);
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
                        "PRODUCT_UPDATED",
                        hdct.getIdHoaDon().getId(),   // ✅ Không phải hoaDon.getId()
                        spct.getId(),
                        spct.getSoLuongTon()
                )
        );
    }

    @Override
    @Transactional
    public void capNhatSoLuong(Integer idHoaDonChiTiet, Integer soLuongMoi) {

        if (soLuongMoi <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        HoaDonChiTiet hdct = ctRepo.findById(idHoaDonChiTiet)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        SanPhamChiTiet spct = hdct.getIdSanPhamChiTiet();

        int soLuongCu = hdct.getSoLuong();

        if (soLuongMoi == soLuongCu) {
            return;
        }

        int chenhLech = soLuongMoi - soLuongCu;

        // tăng số lượng
        if (chenhLech > 0) {

            if (spct.getSoLuongTon() < chenhLech) {
                throw new RuntimeException("Không đủ tồn kho");
            }

            spct.setSoLuongTon(
                    spct.getSoLuongTon() - chenhLech
            );

        } else {

            // giảm số lượng
            spct.setSoLuongTon(
                    spct.getSoLuongTon() + Math.abs(chenhLech)
            );
        }

        hdct.setSoLuong(soLuongMoi);

        hdct.setThanhTien(
                hdct.getDonGia()
                        .multiply(BigDecimal.valueOf(soLuongMoi))
        );

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
                        "PRODUCT_UPDATED",
                        idHoaDon,
                        spct.getId(),
                        spct.getSoLuongTon()
                )
        );
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

        hoaDon.setNgayCapNhat(LocalDateTime.now());
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


        hd.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hd);
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

        DiaChiKhachHang diaChi =
                diaChiKhachHangRepository.findByIdKhachHang_IdAndMacDinhTrue(khachHang.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Khách hàng chưa có địa chỉ"));

        String diaChiDayDu =
                diaChi.getDiaChiCuThe()
                        + ", "
                        + diaChi.getPhuong()
                        + ", "
                        + diaChi.getQuan()
                        + ", "
                        + diaChi.getThanhPho();

        hd.setDiaChiGiaoHang(diaChiDayDu);

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

        hd = hoaDonRepo.findById(hd.getId())
                .orElseThrow();

        String qrUrl = VietQrUtil.createQrUrl(
                hd.getTongThanhToan().longValue(),
                hd.getMaHoaDon()
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

        map.put("qrUrl", qrUrl);
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

    @Transactional
    public void updateTrangThai(Integer id, String trangThaiMoi) {

        HoaDon hoaDon = hoaDonRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        TrangThaiHoaDon newStatus;
        try {
            newStatus = TrangThaiHoaDon.valueOf(trangThaiMoi.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        }

        String current = hoaDon.getTrangThai().toUpperCase();

        if ("DA_HUY".equals(current)) {
            throw new RuntimeException("Đơn đã huỷ không thể cập nhật");
        }

        if (!isValidTransition(current, newStatus.name())) {
            throw new RuntimeException(
                    "Không thể chuyển từ " + current + " sang " + newStatus
            );
        }

        hoaDon.setTrangThai(newStatus.name());
        hoaDon.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepo.save(hoaDon);
    }

    private static final Set<String> CHO_XAC_NHAN =
            Set.of("DA_XAC_NHAN", "DA_HUY");

    private static final Set<String> DA_XAC_NHAN =
            Set.of("DANG_GIAO", "DA_HUY");

    private static final Set<String> DANG_GIAO =
            Set.of("HOAN_THANH");

    private static final Set<String> DA_GIAO =
            Set.of(); // hoặc bỏ luôn nếu không dùng

    private boolean isValidTransition(String from, String to) {

        from = from.toUpperCase();
        to = to.toUpperCase();

        return switch (from) {

            case "CHO_XAC_NHAN" -> CHO_XAC_NHAN.contains(to);

            case "DA_XAC_NHAN" -> DA_XAC_NHAN.contains(to);

            case "DANG_GIAO" -> DANG_GIAO.contains(to);

            case "DA_GIAO" -> DA_GIAO.contains(to);

            case "HOAN_THANH", "DA_HUY" -> false;

            default -> false;
        };
    }

    private void kiemTraVoucherConHopLe(Integer idHoaDon) {

        HoaDonVoucher hdVoucher =
                hoaDonVoucherRepo.findByIdHoaDon_Id(idHoaDon)
                        .orElse(null);

        if (hdVoucher == null) {
            return;
        }

        HoaDon hd = hoaDonRepo.findById(idHoaDon).orElseThrow();

        Voucher voucher = hdVoucher.getIdVoucher();

        // hết lượt
        if (voucher.getSoLuongDaDung() >= voucher.getSoLuong()) {
            hoaDonVoucherRepo.delete(hdVoucher);

            posSocketService.send(
                    new PosEvent(
                            "VOUCHER_REMOVED",
                            idHoaDon,
                            voucher.getId(),
                            null
                    )
            );

            return;
        }

        // bị khóa
        if (voucher.getTrangThai() == 0) {
            hoaDonVoucherRepo.delete(hdVoucher);

            posSocketService.send(
                    new PosEvent(
                            "VOUCHER_REMOVED",
                            idHoaDon,
                            voucher.getId(),
                            null
                    )
            );

            return;
        }

        LocalDateTime now = LocalDateTime.now();

        // chưa tới ngày
        if (voucher.getNgayBatDau().isAfter(now)) {
            hoaDonVoucherRepo.delete(hdVoucher);

            posSocketService.send(
                    new PosEvent(
                            "VOUCHER_REMOVED",
                            idHoaDon,
                            voucher.getId(),
                            null
                    )
            );

            return;
        }

        // hết hạn
        if (voucher.getNgayKetThuc().isBefore(now)) {
            hoaDonVoucherRepo.delete(hdVoucher);

            posSocketService.send(
                    new PosEvent(
                            "VOUCHER_REMOVED",
                            idHoaDon,
                            voucher.getId(),
                            null
                    )
            );

            return;
        }

        // không đủ giá trị đơn
        if (hd.getTongTienHang().compareTo(voucher.getGiaTriDonHangToiThieu()) < 0) {
            hoaDonVoucherRepo.delete(hdVoucher);

            posSocketService.send(
                    new PosEvent(
                            "VOUCHER_REMOVED",
                            idHoaDon,
                            voucher.getId(),
                            null
                    )
            );

            return;
        }
    }

    @Override
    public Map<String, Object> taoQr(Integer hoaDonId) {

        HoaDon hd = hoaDonRepo.findById(hoaDonId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));


        // Không cho tạo QR nếu hóa đơn đã hủy

        String qrUrl = VietQrUtil.createQrUrl(
                hd.getTongThanhToan().longValue(),
                hd.getMaHoaDon()
        );

        Map<String, Object> result = new HashMap<>();

        result.put("id", hd.getId());
        result.put("maHoaDon", hd.getMaHoaDon());
        result.put("tongTien", hd.getTongThanhToan());
        result.put("qrUrl", qrUrl);

        return result;
    }

    @Override
    @Transactional
    public void capNhatHoaDonTheoVoucher(Integer idVoucher){

        List<HoaDonVoucher> list =
                hoaDonVoucherRepo.findByIdVoucher_Id(idVoucher);


        for(HoaDonVoucher hdv : list){


            Integer idHoaDon =
                    hdv.getIdHoaDon().getId();


            // kiểm tra voucher còn hợp lệ không
            kiemTraVoucherConHopLe(idHoaDon);


            // tính lại tiền
            recalculateHoaDon(idHoaDon);

        }
    }
}
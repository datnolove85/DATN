package com.example.backend.Service.impl;

import com.example.backend.Entity.*;

import com.example.backend.Repository.*;
import com.example.backend.Request.*;
import com.example.backend.Response.GioHangResponse;
import com.example.backend.Response.HoaDonChiTietResponse;
import com.example.backend.Response.HoaDonDetailResponse;
import com.example.backend.Response.HoaDonResponse;
import com.example.backend.Service.HoaDonService;
import com.example.backend.mapper.HoaDonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
    @Override
    public Object thanhToanHoaDon(
            ThanhToanHoaDonRequest req
    ) {

        HoaDon hd = hoaDonRepo.findById(
                req.getIdHoaDon()
        ).orElseThrow(() ->
                new RuntimeException("Không tìm thấy hóa đơn"));

        if (!"cho_xac_nhan".equals(hd.getTrangThai())) {
            throw new RuntimeException(
                    "Hóa đơn đã được thanh toán"
            );
        }

        List<HoaDonChiTiet> dsChiTiet =
                ctRepo.findByIdHoaDon_Id(
                        hd.getId()
                );

        if (dsChiTiet.isEmpty()) {
            throw new RuntimeException(
                    "Hóa đơn chưa có sản phẩm"
            );
        }

        // ======================
        // TÍNH TỔNG TIỀN
        // ======================

        BigDecimal tongTienHang =
                dsChiTiet.stream()
                        .map(HoaDonChiTiet::getThanhTien)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal tongGiamGia =
                BigDecimal.ZERO;

        // ======================
        // VOUCHER
        // ======================

        if (req.getIdVoucher() != null) {

            Voucher voucher =
                    voucherRepo.findById(
                            req.getIdVoucher()
                    ).orElseThrow(() ->
                            new RuntimeException(
                                    "Không tìm thấy voucher"
                            ));

            if (voucher.getSoLuongDaDung() >= voucher.getSoLuong()) {
                throw new RuntimeException(
                        "Voucher đã hết lượt sử dụng"
                );
            }

            if (
                    tongTienHang.compareTo(
                            voucher.getGiaTriDonHangToiThieu()
                    ) < 0
            ) {
                throw new RuntimeException(
                        "Đơn hàng chưa đạt giá trị tối thiểu"
                );
            }

            if ("tien_mat".equals(
                    voucher.getLoaiGiamGia()
            )) {

                tongGiamGia =
                        voucher.getGiaTriGiam();

            } else {

                tongGiamGia =
                        tongTienHang.multiply(
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

            HoaDonVoucher hdVoucher =
                    new HoaDonVoucher();

            hdVoucher.setIdHoaDon(hd);

            hdVoucher.setIdVoucher(
                    voucher
            );

            hdVoucher.setSoTienGiam(
                    tongGiamGia
            );

            hoaDonVoucherRepo.save(
                    hdVoucher
            );

            voucher.setSoLuongDaDung(
                    voucher.getSoLuongDaDung() + 1
            );

            voucherRepo.save(
                    voucher
            );
        }
        // ======================
        // TỔNG THANH TOÁN
        // ======================

        BigDecimal tongThanhToan =
                tongTienHang.subtract(
                        tongGiamGia
                );

        if (tongThanhToan.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            tongThanhToan =
                    BigDecimal.ZERO;
        }

        // ======================
        // THANH TOÁN
        // ======================

        PhuongThucThanhToan pt =
                ptRepo.findById(
                        req.getIdPhuongThucThanhToan()
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy phương thức"
                        ));

        ThanhToan tt =
                new ThanhToan();

        tt.setIdHoaDon(hd);

        tt.setIdPhuongThucThanhToan(pt);

        tt.setSoTien(
                tongThanhToan
        );

        tt.setTrangThai(
                "da_thanh_toan"
        );

        tt.setNgayThanhToan(
                Instant.now()
        );

        ttRepo.save(tt);

        // ======================
        // UPDATE HÓA ĐƠN
        // ======================

        hd.setTongTienHang(
                tongTienHang
        );

        hd.setTongGiamGia(
                tongGiamGia
        );

        hd.setTongThanhToan(
                tongThanhToan
        );

        hd.setTrangThai(
                "da_giao"
        );

        hd.setNgayCapNhat(
                Instant.now()
        );

        hoaDonRepo.save(hd);

        // ======================
        // RESPONSE
        // ======================

        Map<String, Object> res =
                new HashMap<>();

        res.put("id", hd.getId());

        res.put("maHoaDon",
                hd.getMaHoaDon());

        res.put("tongTienHang",
                tongTienHang);

        res.put("tongGiamGia",
                tongGiamGia);

        res.put("tongThanhToan",
                tongThanhToan);

        return res;
    }
    @Override
    public List<HoaDonResponse> getAll() {
        return hoaDonRepo.getAllResponse();
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



    public HoaDonDetailResponse getDetail(Integer id) {

        HoaDon hd = hoaDonRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hóa đơn"));

        List<HoaDonChiTietResponse> sanPhams =
                ctRepo.findByIdHoaDon_Id(id)
                        .stream()
                        .map(ct -> {

                            SanPhamChiTiet spct =
                                    ct.getIdSanPhamChiTiet();

                            return new HoaDonChiTietResponse(
                                    ct.getId(),
                                    spct.getId(),
                                    spct.getMaSanPhamChiTiet(),
                                    spct.getTenSanPhamChiTiet(),
                                    spct.getIdMauSac().getTenMauSac(),
                                    spct.getIdKichThuoc().getTenKichThuoc(),
                                    ct.getSoLuong(),
                                    ct.getDonGia(),
                                    ct.getThanhTien()
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

        return response;
    }

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

        hoaDon.setNgayTao(Instant.now());
        hoaDon.setNgayCapNhat(Instant.now());

        return hoaDonRepo.save(hoaDon);
    }


    @Override
    public List<HoaDon> getHoaDonCho() {

        return hoaDonRepo.findByTrangThaiOrderByNgayTaoDesc(
                "cho_xac_nhan"
        );
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

        // cập nhật tổng tiền hóa đơn
        List<HoaDonChiTiet> ds =
                ctRepo.findByIdHoaDon_Id(
                        hoaDon.getId()
                );

        BigDecimal tongTien =
                ds.stream()
                        .map(HoaDonChiTiet::getThanhTien)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        hoaDon.setTongTienHang(
                tongTien
        );

        hoaDon.setTongThanhToan(
                tongTien
        );

        hoaDonRepo.save(
                hoaDon
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

        capNhatTongTienHoaDon(
                hdct.getIdHoaDon().getId()
        );
    }

    private void capNhatTongTienHoaDon(
            Integer idHoaDon
    ) {

        HoaDon hd = hoaDonRepo.findById(idHoaDon)
                .orElseThrow();

        List<HoaDonChiTiet> ds =
                ctRepo.findByIdHoaDon_Id(idHoaDon);

        BigDecimal tongTien =
                ds.stream()
                        .map(HoaDonChiTiet::getThanhTien)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        hd.setTongTienHang(tongTien);
        hd.setTongThanhToan(tongTien);

        hoaDonRepo.save(hd);
    }

    @Override
    public List<GioHangResponse> getChiTietHoaDon(Integer idHoaDon) {

        List<HoaDonChiTiet> ds = ctRepo.findByIdHoaDon_Id(idHoaDon);

        return ds.stream()
                .map(ct -> new GioHangResponse(

                        ct.getId(),

                        ct.getIdSanPhamChiTiet(), // ✅ truyền cả object

                        ct.getIdSanPhamChiTiet().getTenSanPhamChiTiet(),

                        ct.getDonGia(),

                        ct.getSoLuong(),

                        ct.getThanhTien()
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

        capNhatTongTienHoaDon(idHoaDon);
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

        capNhatTongTienHoaDon(
                hdct.getIdHoaDon().getId()
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

        hoaDonRepo.save(hoaDon);
    }

    @Transactional
    @Override
    public void huyHoaDon(Integer idHoaDon) {

        // 1. Lấy hóa đơn
        HoaDon hoaDon = hoaDonRepo.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        // 2. Chặn nếu đã thanh toán
        if (hoaDon.getTrangThai().equalsIgnoreCase("da_giao")) {
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
        hoaDonRepo.save(hoaDon);
    }
}
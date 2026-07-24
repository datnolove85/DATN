package com.example.backend.Service.impl;

import com.example.backend.Repository.*;
import com.example.backend.Response.thongke.*;
import com.example.backend.Service.ThongKeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThongKeServiceImpl implements ThongKeService {

    private final HoaDonRepository hoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private final SanPhamRepository sanPhamRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final HinhAnhRepository hinhAnhRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final ThanhToanRepository thanhToanRepository;

    @Override
    public DashboardResponse dashboard(LocalDate from, LocalDate to, String loaiHoaDon) {

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);

        DashboardResponse response = new DashboardResponse();

        // ================== DOANH THU ==================
        BigDecimal doanhThu =
                hoaDonRepository.getTongDoanhThuTheoKhoang(start, end, loaiHoaDon);

        response.setTongDoanhThu(doanhThu);

        response.setDoanhThuTienMat(
                thanhToanRepository.tongTienMatTheoKhoang(start, end, loaiHoaDon));

        response.setDoanhThuChuyenKhoan(
                thanhToanRepository.tongChuyenKhoanTheoKhoang(start, end, loaiHoaDon));

        // ================== GIÁ VỐN ==================
        BigDecimal giaVon =
                hoaDonChiTietRepository.tongGiaVonTheoKhoang(start, end, loaiHoaDon);

        response.setGiaVon(giaVon);

        // ================== LỢI NHUẬN ==================
        BigDecimal loiNhuan = doanhThu.subtract(giaVon);

        response.setLoiNhuanGop(loiNhuan);

        if (doanhThu.compareTo(BigDecimal.ZERO) > 0) {
            response.setBienLoiNhuan(
                    loiNhuan.multiply(BigDecimal.valueOf(100))
                            .divide(doanhThu, 2, java.math.RoundingMode.HALF_UP)
            );
        } else {
            response.setBienLoiNhuan(BigDecimal.ZERO);
        }

        // ================== ĐƠN HÀNG ==================
        Long tongDon =
                hoaDonRepository.countHoaDonTheoKhoang(start, end, loaiHoaDon);

        response.setTongDonHang(tongDon);

        response.setDonHangOnline(
                hoaDonRepository.countHoaDonOnlineTheoKhoang(start, end));

        response.setDonHangTaiQuay(
                hoaDonRepository.countHoaDonTaiQuayTheoKhoang(start, end));

        // ================== KHÁCH HÀNG ==================
        response.setTongKhachHang(
                khachHangRepository.countKhachHang());

        response.setKhachThanhVien(
                khachHangRepository.countKhachThanhVien());

        response.setKhachLe(
                hoaDonRepository.countKhachLeTheoKhoang(start, end));

        // ================== SẢN PHẨM ==================
        Long tongSoLuongBan =
                hoaDonChiTietRepository.tongSoLuongBanTheoKhoang(
                        start,
                        end,
                        loaiHoaDon);

        response.setTongSanPham(tongSoLuongBan);

        response.setSkuDaBan(
                hoaDonChiTietRepository.countSkuDaBanTheoKhoang(
                        start,
                        end,
                        loaiHoaDon));

        if (tongDon > 0) {
            response.setTrungBinhSpMoiDon(
                    BigDecimal.valueOf(tongSoLuongBan)
                            .divide(BigDecimal.valueOf(tongDon), 2, java.math.RoundingMode.HALF_UP)
            );
        } else {
            response.setTrungBinhSpMoiDon(BigDecimal.ZERO);
        }

        return response;
    }

    @Override
    public List<RevenueResponse> revenue(
            LocalDate from,
            LocalDate to,
            String groupBy,
            String loaiHoaDon
    ) {
        List<Object[]> rows;
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);

        switch (groupBy) {
            case "month":
                rows = hoaDonRepository.revenueByMonth(start, end, loaiHoaDon);
                break;
            case "year":
                // Đã truyền thêm loaiHoaDon vào đây để đồng bộ với Repository
                rows = hoaDonRepository.revenueByYear(loaiHoaDon);
                break;
            default:
                rows = hoaDonRepository.revenueByDay(start, end, loaiHoaDon);
        }

        return rows.stream()
                .map(r -> new RevenueResponse(
                        r[0].toString(),
                        (BigDecimal) r[1]
                ))
                .toList();
    }

    @Override
    public List<OrderStatusResponse> thongKeTrangThai(LocalDate from, LocalDate to, String loaiHoaDon) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);
        return hoaDonRepository.thongKeTrangThaiTheoKhoang(start, end, loaiHoaDon);
    }

    @Override
    public List<TopProductResponse> topProducts(LocalDate from, LocalDate to, Integer limit, String loaiHoaDon) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);

        // Đã truyền thêm loaiHoaDon vào đây
        List<TopProductStatistic> statistics =
                hoaDonChiTietRepository.topProductsTheoKhoang(start, end, loaiHoaDon, PageRequest.of(0, limit));

        if (statistics.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> ids = statistics.stream()
                .map(TopProductStatistic::getProductId)
                .toList();

        Map<Integer, ProductExtraInfo> extraMap =
                sanPhamChiTietRepository.getExtraInfo(ids)
                        .stream()
                        .collect(Collectors.toMap(
                                ProductExtraInfo::getProductId,
                                Function.identity()
                        ));

        Map<Integer, String> imageMap =
                hinhAnhRepository.getMainImages(ids)
                        .stream()
                        .collect(Collectors.toMap(
                                h -> h.getIdSanPhamChiTiet().getIdSanPham().getId(),
                                h -> "/sanpham/" + h.getLink(),
                                (a, b) -> a
                        ));

        List<TopProductResponse> result = new ArrayList<>();

        for (TopProductStatistic s : statistics) {
            TopProductResponse dto = new TopProductResponse();

            dto.setProductId(s.getProductId());
            dto.setMaSanPham(s.getMaSanPham());
            dto.setTenSanPham(s.getTenSanPham());

            // Gán thêm thông tin thương hiệu và chất liệu
            dto.setThuongHieu(s.getThuongHieu());
            dto.setChatLieu(s.getChatLieu());

            dto.setSoLuongBan(s.getSoLuongBan());
            dto.setDoanhThu(s.getDoanhThu());
            dto.setLoiNhuan(
                    s.getDoanhThu().subtract(s.getGiaVon())
            );

            ProductExtraInfo extra = extraMap.get(s.getProductId());

            if (extra != null) {
                dto.setGiaBan(extra.getGiaBan());
                dto.setTongTonKho(extra.getTongTonKho());
                dto.setSoBienThe(extra.getSoBienThe());
            }

            dto.setAnh(imageMap.get(s.getProductId()));
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<TopCustomerResponse> topCustomers(LocalDate from, LocalDate to, Integer limit, String loaiHoaDon) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);

        return hoaDonRepository.topCustomersTheoKhoang(
                start,
                end,
                PageRequest.of(0, limit),
                loaiHoaDon
        );
    }
}
package com.example.backend.Service.impl;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Response.*;
import com.example.backend.Service.DonHangService;

import com.example.backend.Service.payment.VoucherConsumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DonHangServiceImpl implements DonHangService {

    private final KhachHangRepository khachHangRepository;
    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final ThanhToanRepository thanhToanRepository;
    private final HinhAnhRepository hinhAnhRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final HoaDonVoucherRepository hoaDonVoucherRepository;
    private final VoucherRepository voucherRepository;
    private final VoucherCuaKhachHangRepository voucherCuaKhachHangRepository;
    private final KhoVoucherRepository khoVoucherRepository;
    private final VoucherConsumeService voucherConsumeService;
private  final LichSuHoaDonRepository lichSuHoaDonRepository;

    @Override
    public List<DonHangResponse> layDanhSachDonHang(Integer idTaiKhoan) {
        KhachHang khachHang = khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        List<HoaDon> hoaDons = hoaDonRepository
                .findByIdKhachHang_Id(khachHang.getId())
                .stream()
                .filter(hd -> "ONLINE".equalsIgnoreCase(hd.getLoaiHoaDon()))
                .toList();

        List<DonHangResponse> result = new ArrayList<>();
        for (HoaDon hoaDon : hoaDons) {
            result.add(convertDonHang(hoaDon));
        }

        return result;
    }

    @Override
    public DonHangResponse layChiTietDonHang(
            Integer idTaiKhoan,
            Integer idHoaDon
    ) {
        KhachHang khachHang = khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        HoaDon hoaDon = hoaDonRepository
                .findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        if (!hoaDon.getIdKhachHang().getId().equals(khachHang.getId())) {
            throw new RuntimeException("Đơn hàng không thuộc khách hàng");
        }

        return convertDonHang(hoaDon);
    }

    @Override
    public DonHangResponse layChiTietDonHangChoAdmin(Integer idHoaDon) {
        HoaDon hoaDon = hoaDonRepository
                .findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        return convertDonHang(hoaDon);
    }

    private DonHangResponse convertDonHang(HoaDon hoaDon) {
        DonHangResponse response = new DonHangResponse();

        /*
         * THÔNG TIN ĐƠN HÀNG
         */
        DonHangInfoDTO info = new DonHangInfoDTO();
        info.setId(hoaDon.getId());
        info.setMaHoaDon(hoaDon.getMaHoaDon());
        info.setLoaiHoaDon(hoaDon.getLoaiHoaDon());
        info.setTrangThai(hoaDon.getTrangThai());
        info.setTrangThaiHienThi(hienThiTrangThai(hoaDon.getTrangThai()));
        info.setTrangThaiThanhToan(hoaDon.getTrangThaiThanhToan());
        info.setTrangThaiThanhToanHienThi(hienThiThanhToan(hoaDon.getTrangThaiThanhToan()));
        info.setTongTienHang(hoaDon.getTongTienHang());
        info.setTongGiamGia(hoaDon.getTongGiamGia());
        info.setPhiVanChuyen(hoaDon.getPhiVanChuyen());
        info.setTongThanhToan(hoaDon.getTongThanhToan());
        info.setGhiChu(hoaDon.getGhiChu());
        info.setNgayTao(hoaDon.getNgayTao());
        info.setNgayCapNhat(hoaDon.getNgayCapNhat());
        response.setThongTinDonHang(info);

        /*
         * NGƯỜI NHẬN
         */
        NguoiNhanDTO nguoiNhan = new NguoiNhanDTO();
        nguoiNhan.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
        nguoiNhan.setSoDienThoai(hoaDon.getSoDienThoaiNguoiNhan());
        nguoiNhan.setDiaChi(hoaDon.getDiaChiGiaoHang());
        response.setNguoiNhan(nguoiNhan);

        /*
         * THEO DÕI ĐƠN
         */
        TheoDoiDonHangDTO theoDoi = new TheoDoiDonHangDTO();
        String trangThai = hoaDon.getTrangThai() != null ? hoaDon.getTrangThai().toLowerCase() : "";

        if ("da_huy".equals(trangThai)) {
            theoDoi.setDaHuy(true);
        } else if ("giao_that_bai".equals(trangThai)) {
            theoDoi.setChoXacNhan(true);
            theoDoi.setDaXacNhan(true);
            theoDoi.setChoVanChuyen(true);
            theoDoi.setDangGiao(true);
            theoDoi.setGiaoThatBai(true);
        } else {
            switch (trangThai) {
                case "hoan_thanh":
                    theoDoi.setHoanThanh(true);
                case "giao_thanh_cong":
                    theoDoi.setGiaoThanhCong(true);
                case "dang_giao":
                    theoDoi.setDangGiao(true);
                case "cho_van_chuyen":
                    theoDoi.setChoVanChuyen(true);
                case "da_xac_nhan":
                    theoDoi.setDaXacNhan(true);
                case "cho_xac_nhan":
                    theoDoi.setChoXacNhan(true);
                    break;
            }
        }
        response.setTheoDoi(theoDoi);

        /*
         * SẢN PHẨM
         */
        List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepository.findByIdHoaDon_Id(hoaDon.getId());
        List<DonHangChiTietDTO> sanPham = new ArrayList<>();

        for (HoaDonChiTiet ct : chiTietList) {
            DonHangChiTietDTO dto = new DonHangChiTietDTO();
            SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();

            dto.setIdHoaDonChiTiet(ct.getId());
            dto.setIdSanPhamChiTiet(spct.getId());
            dto.setIdSanPham(spct.getIdSanPham().getId());
            dto.setMaSanPham(spct.getMaSanPhamChiTiet());
            dto.setTenSanPham(spct.getIdSanPham().getTenSanPham());
            dto.setMauSac(spct.getIdMauSac().getTenMauSac());
            dto.setKichThuoc(spct.getIdKichThuoc().getTenKichThuoc());
            dto.setSoLuong(ct.getSoLuong());
            dto.setDonGia(ct.getDonGia());
            dto.setThanhTien(ct.getThanhTien());
            dto.setSoLuongKhaDung(spct.getSoLuongKhaDung());
            dto.setAnh(
                    hinhAnhRepository
                            .findFirstByIdSanPhamChiTiet_IdAndLaAnhChinhTrue(spct.getId())
                            .map(hinhAnh -> "/sanpham/" + hinhAnh.getLink())
                            .orElse(null)
            );

            sanPham.add(dto);
        }
        response.setSanPham(sanPham);

        /*
         * THANH TOÁN
         */
        Optional<ThanhToan> thanhToanOptional = thanhToanRepository.findFirstByIdHoaDon_Id(hoaDon.getId());

        if (thanhToanOptional.isPresent()) {
            ThanhToan thanhToan = thanhToanOptional.get();
            ThanhToanDTO dto = new ThanhToanDTO();
            dto.setMaGiaoDich(thanhToan.getMaGiaoDich());
            dto.setSoTien(thanhToan.getSoTien());
            dto.setTrangThai(thanhToan.getTrangThai());
            dto.setNgayThanhToan(thanhToan.getNgayThanhToan());
            response.setThanhToan(dto);
        }

        return response;
    }

    private String hienThiTrangThai(String trangThai) {
        if (trangThai == null) return "";

        return switch (trangThai.toLowerCase()) {
            case "cho_xac_nhan" -> "Chờ xác nhận";
            case "da_xac_nhan" -> "Đã xác nhận";
            case "cho_van_chuyen" -> "Chờ vận chuyển";
            case "dang_giao" -> "Đang giao";
            case "giao_thanh_cong" -> "Giao thành công";
            case "giao_that_bai" -> "Giao thất bại";
            case "hoan_thanh" -> "Hoàn thành";
            case "da_huy" -> "Đã hủy";
            default -> "Khác";
        };
    }

    private String hienThiThanhToan(String value) {
        if ("da_thanh_toan".equals(value)) {
            return "Đã thanh toán";
        }
        if ("hoan_tien".equals(value)) {
            return "Đã hoàn tiền";
        }
        return "Chưa thanh toán";
    }

    @Override
    @Transactional
    public void xacNhanDaNhan(Integer id) {

        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        if (!"giao_thanh_cong".equals(hoaDon.getTrangThai())) {
            throw new RuntimeException("Đơn hàng chưa thể xác nhận hoàn thành.");
        }

        hoaDon.setTrangThai("hoan_thanh");
        hoaDon.setNgayCapNhat(LocalDateTime.now());

        hoaDonRepository.save(hoaDon);
    }

    @Transactional
    public void huyDonHang(Integer idTaiKhoan, Integer idHoaDon, String lyDoHuy) {
        KhachHang khachHang = khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin khách hàng"));

        HoaDon hoaDon = hoaDonRepository
                .findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        if (hoaDon.getIdKhachHang() == null || !hoaDon.getIdKhachHang().getId().equals(khachHang.getId())) {
            throw new RuntimeException("Bạn không có quyền hủy đơn hàng này!");
        }

        String trangThai = hoaDon.getTrangThai() != null ? hoaDon.getTrangThai().toLowerCase() : "";

        if (!"cho_xac_nhan".equals(trangThai) && !"da_xac_nhan".equals(trangThai)) {
            Map<String, Object> socketPayload = new HashMap<>();
            socketPayload.put("type", "CANCEL_REJECTED");
            socketPayload.put("idHoaDon", idHoaDon);
            socketPayload.put("trangThaiMoi", hoaDon.getTrangThai());
            socketPayload.put("message", "Đơn hàng đã chuyển sang trạng thái '" + hoaDon.getTrangThai() + "', không thể hủy!");

            messagingTemplate.convertAndSend("/topic/orders", (Object) socketPayload);

            throw new RuntimeException("Đơn hàng đang giao hoặc đã hoàn tất, không thể hủy!");
        }


        // 1. Lưu lại trạng thái cũ trước khi đổi
        String trangThaiCu = hoaDon.getTrangThai();

        // 2. Cập nhật trạng thái mới cho hóa đơn
        hoaDon.setTrangThai("da_huy");
        String noiDungGhiChu = lyDoHuy != null && !lyDoHuy.trim().isEmpty()
                ? "Khách hủy: " + lyDoHuy
                : "Khách tự hủy đơn hàng";
        hoaDon.setGhiChu(noiDungGhiChu);
        hoaDon.setNgayCapNhat(LocalDateTime.now());

        // 3. 🔴 TẠO VÀ LƯU LỊCH SỬ HÓA ĐƠN ĐÚNG VỚI ENTITY CỦA BẠN
        LichSuHoaDon lichSu = new LichSuHoaDon();
        lichSu.setHoaDon(hoaDon);
        lichSu.setTrangThaiCu(trangThaiCu);
        lichSu.setTrangThaiMoi("da_huy");
        lichSu.setThoiGian(LocalDateTime.now());
        lichSu.setNguonThaoTac("CUSTOMER"); // Nguồn là khách hàng
        lichSu.setNhanVien(null);           // Do khách hủy nên không gắn nhân viên nào cả
        lichSu.setGhiChu(noiDungGhiChu);

        lichSuHoaDonRepository.save(lichSu);

        List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepository.findByIdHoaDon_Id(hoaDon.getId());
        for (HoaDonChiTiet ct : chiTietList) {
            SanPhamChiTiet spct = ct.getIdSanPhamChiTiet();
            if (spct != null) {
                int soLuongTra = ct.getSoLuong() != null ? ct.getSoLuong() : 0;

                int tamGiuHienTai = spct.getSoLuongTamGiu() != null ? spct.getSoLuongTamGiu() : 0;
                spct.setSoLuongTamGiu(Math.max(0, tamGiuHienTai - soLuongTra));

                if ("da_xac_nhan".equals(trangThai)) {
                    int tonHienTai = spct.getSoLuongTon() != null ? spct.getSoLuongTon() : 0;
                    spct.setSoLuongTon(tonHienTai + soLuongTra);
                }
            }
        }

        HoaDonVoucher hdVoucher = hoaDonVoucherRepository
                .findByIdHoaDon_Id(hoaDon.getId())
                .orElse(null);

        if (hdVoucher != null) {
            if (Boolean.TRUE.equals(hdVoucher.getDaConsume())) {
                if (hdVoucher.getIdVoucher() != null) {
                    Voucher voucher = hdVoucher.getIdVoucher();
                    int daDung = voucher.getSoLuongDaDung() == null
                            ? 0
                            : voucher.getSoLuongDaDung();

                    voucher.setSoLuongDaDung(Math.max(0, daDung - 1));
                    voucher.setSoLuong(voucher.getSoLuong() + 1);

                    voucherRepository.save(voucher);
                }

                if (hdVoucher.getVoucherCuaKhachHang() != null) {
                    VoucherCuaKhachHang voucherKhach =
                            hdVoucher.getVoucherCuaKhachHang();

                    voucherKhach.setTrangThai("CHUA_DUNG");
                    voucherCuaKhachHangRepository.save(voucherKhach);

                    if (hdVoucher.getIdKhoVoucher() != null) {
                        KhoVoucher khoVoucher = hdVoucher.getIdKhoVoucher();
                        int conLai = khoVoucher.getSoLuongConLai() == null
                                ? 0
                                : khoVoucher.getSoLuongConLai();

                        khoVoucher.setSoLuongConLai(conLai + 1);
                        khoVoucherRepository.save(khoVoucher);
                    }
                }
            }
            hoaDonVoucherRepository.delete(hdVoucher);
        }

        hoaDonRepository.save(hoaDon);

        Map<String, Object> successPayload = new HashMap<>();
        successPayload.put("type", "CANCEL_SUCCESS");
        successPayload.put("idHoaDon", idHoaDon);

        messagingTemplate.convertAndSend("/topic/orders", (Object) successPayload);
    }
}
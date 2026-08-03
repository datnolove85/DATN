package com.example.backend.Service.impl;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Response.LichSuXuResponse;
import com.example.backend.Response.TrangThaiGamificationResponse;
import com.example.backend.Response.VoucherCuaKhachHangResponse;
import com.example.backend.Service.GamificationService;
import com.example.backend.Service.HoaDonService;
import com.example.backend.Service.PosSocketService;
import com.example.backend.websocket.PosEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
public class GamificationServiceImpl implements GamificationService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private DiemDanhHangNgayRepository diemDanhHangNgayRepository;

    @Autowired
    private LichSuXuRepository lichSuXuRepository;

    @Autowired
    private PhanThuongMinigameRepository phanThuongMinigameRepository;

    @Autowired
    private LichSuMinigameRepository lichSuMinigameRepository;

    @Autowired
    private VoucherCuaKhachHangRepository voucherCuaKhachHangRepository;

    @Autowired
    private KhoVoucherRepository khoVoucherRepository;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private PosSocketService posSocketService;

    @Override
    @Transactional
    public DiemDanhHangNgay diemDanhHangNgay(Integer idKhachHang) {
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        LocalDate today = LocalDate.now();

        // 1. Kiểm tra hôm nay đã điểm danh chưa
        boolean daDiemDanh = diemDanhHangNgayRepository.existsByIdKhachHang_IdAndNgayDiemDanh(idKhachHang, today);
        if (daDiemDanh) {
            throw new RuntimeException("Hôm nay bạn đã điểm danh rồi!");
        }

        // 2. Tính toán chuỗi Streak nghiêm ngặt
        LocalDate yesterday = today.minusDays(1);
        int chuoiMoi = 1;

        if (khachHang.getNgayDiemDanhGanNhat() != null && khachHang.getNgayDiemDanhGanNhat().equals(yesterday)) {
            // Nếu hôm qua có điểm danh -> Tăng chuỗi lên 1
            chuoiMoi = khachHang.getChuoiDiemDanh() + 1;
        } else {
            // Nếu quên hoặc gián đoạn -> Reset về 1
            chuoiMoi = 1;
        }

        // Thưởng xu tăng dần theo chuỗi (VD: cơ bản 10 xu + 2 xu * chuỗi)
        int soXuNhan = 10 + (chuoiMoi * 2);

        // 3. Cập nhật thông tin Khách hàng
        int soDuTruoc = khachHang.getSoDuXu();
        khachHang.setSoDuXu(soDuTruoc + soXuNhan);
        khachHang.setChuoiDiemDanh(chuoiMoi);
        khachHang.setNgayDiemDanhGanNhat(today);
        khachHangRepository.save(khachHang);

        // 4. Lưu lịch sử điểm danh
        DiemDanhHangNgay diemDanh = new DiemDanhHangNgay();
        diemDanh.setIdKhachHang(khachHang);
        diemDanh.setNgayDiemDanh(today);
        diemDanh.setSoXuNhan(soXuNhan);
        diemDanh.setSoNgayLienTiep(chuoiMoi);
        diemDanhHangNgayRepository.save(diemDanh);

        // 5. Ghi log biến động ví xu
        LichSuXu lichSuXu = new LichSuXu();
        lichSuXu.setIdKhachHang(khachHang);
        lichSuXu.setSoXuThayDoi(soXuNhan);
        lichSuXu.setSoDuTruoc(soDuTruoc);
        lichSuXu.setSoDuSau(khachHang.getSoDuXu());
        lichSuXu.setLoaiGiaoDich("diem_danh");
        lichSuXu.setMoTa("Điểm danh ngày " + today + " (Chuỗi: " + chuoiMoi + " ngày)");
        lichSuXuRepository.save(lichSuXu);

        return diemDanh;
    }

    @Override
    @Transactional
    public PhanThuongMinigame quayThuong(Integer idKhachHang, String loaiGame) {
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        // === GIỚI HẠN LƯỢT CHƠI TRONG NGÀY CHO "LẬT THẺ" (Tối đa 3 lần) ===
        if ("lat_the".equals(loaiGame)) {
            LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
            LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

            long soLuotDaChoi = lichSuMinigameRepository.countByIdKhachHang_IdAndLoaiGameAndNgayTaoBetween(
                    idKhachHang, "lat_the", startOfDay, endOfDay
            );

            if (soLuotDaChoi >= 3) {
                throw new RuntimeException("Bạn đã đạt giới hạn lật thẻ tối đa 3 lần trong ngày hôm nay!");
            }
        }
        // ================================================================

        // Lấy danh sách phần thưởng hợp lệ theo loại game
        List<PhanThuongMinigame> dsPhanThuong = phanThuongMinigameRepository.findByLoaiGameAndTrangThai(loaiGame, true);
        if (dsPhanThuong.isEmpty()) {
            throw new RuntimeException("Chưa cấu hình phần thưởng cho minigame này!");
        }

        // Thuật toán quay thưởng ngẫu nhiên theo tỷ lệ phần trăm (Probability Weight)
        double tongTyLe = dsPhanThuong.stream().mapToDouble(p -> p.getTyLeTrung().doubleValue()).sum();
        double randomVal = new Random().nextDouble() * tongTyLe;

        double currentSum = 0;
        PhanThuongMinigame phanThuongTrung = dsPhanThuong.get(0);

        for (PhanThuongMinigame pt : dsPhanThuong) {
            currentSum += pt.getTyLeTrung().doubleValue();
            if (randomVal <= currentSum) {
                phanThuongTrung = pt;
                break;
            }
        }

        // Xử lý khi trúng thưởng Xu
        if ("xu".equals(phanThuongTrung.getLoaiPhanThuong())) {
            int soDuTruoc = khachHang.getSoDuXu();
            int soXuThuong = phanThuongTrung.getGiaTriXu();

            khachHang.setSoDuXu(soDuTruoc + soXuThuong);
            khachHangRepository.save(khachHang);

            // Ghi log xu
            LichSuXu logXu = new LichSuXu();
            logXu.setIdKhachHang(khachHang);
            logXu.setSoXuThayDoi(soXuThuong);
            logXu.setSoDuTruoc(soDuTruoc);
            logXu.setSoDuSau(khachHang.getSoDuXu());
            logXu.setLoaiGiaoDich("vong_quay");
            logXu.setMoTa("Trúng phần thưởng minigame: " + phanThuongTrung.getTenPhanThuong());
            lichSuXuRepository.save(logXu);
        }

        // Lưu lịch sử quay minigame (Cột thời gian ngayTao sẽ tự động lưu mốc thời gian hiện tại)
        LichSuMinigame lichSuGame = new LichSuMinigame();
        lichSuGame.setIdKhachHang(khachHang);
        lichSuGame.setIdPhanThuong(phanThuongTrung);
        lichSuGame.setLoaiGame(loaiGame);
        lichSuMinigameRepository.save(lichSuGame);

        return phanThuongTrung;
    }

    @Override
    public TrangThaiGamificationResponse getTrangThaiGamification(Integer idKhachHang) {
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        LocalDate today = LocalDate.now();
        boolean daDiemDanh = diemDanhHangNgayRepository.existsByIdKhachHang_IdAndNgayDiemDanh(idKhachHang, today);

        // === TÍNH SỐ LƯỢT LẬT THẺ CÒN LẠI TRONG NGÀY ===
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        long soLuotDaChoi = lichSuMinigameRepository.countByIdKhachHang_IdAndLoaiGameAndNgayTaoBetween(
                idKhachHang, "lat_the", startOfDay, endOfDay
        );
        int soLuotLatTheConLai = Math.max(0, 3 - (int) soLuotDaChoi);
        // ===============================================

        return new TrangThaiGamificationResponse(
                khachHang.getId(),
                khachHang.getSoDuXu() != null ? khachHang.getSoDuXu() : 0,
                khachHang.getChuoiDiemDanh() != null ? khachHang.getChuoiDiemDanh() : 0,
                khachHang.getNgayDiemDanhGanNhat(),
                daDiemDanh,
                soLuotLatTheConLai // Truyền thêm số lượt vào constructor Response
        );
    }

    @Override
    public List<LichSuXuResponse> getLichSuXu(Integer idKhachHang) {
        List<LichSuXu> list = lichSuXuRepository.findByIdKhachHang_IdOrderByNgayTaoDesc(idKhachHang);

        return list.stream().map(item -> new LichSuXuResponse(
                item.getId(),
                item.getSoXuThayDoi(),
                item.getSoDuTruoc(),
                item.getSoDuSau(),
                item.getLoaiGiaoDich(),
                item.getMoTa(),
                item.getNgayTao()
        )).toList();
    }

    @Override
    public List<PhanThuongMinigame> getAllPhanThuong() {
        return phanThuongMinigameRepository.findAll();
    }

    // === ADMIN: Thêm phần thưởng mới ===
    @Override
    // === ADMIN: Thêm mới phần thưởng ===
    @Transactional
    public PhanThuongMinigame createPhanThuong(PhanThuongMinigame phanThuong) {
        // Làm sạch dữ liệu theo loại quà
        if ("voucher".equals(phanThuong.getLoaiPhanThuong())) {
            phanThuong.setGiaTriXu(0); // Quà voucher thì giá trị xu bằng 0
        } else if ("xu".equals(phanThuong.getLoaiPhanThuong())) {
            phanThuong.setIdVoucher(null); // Quà xu thì không gắn id_voucher
        } else {
            // Loại không trúng hoặc loại khác
            phanThuong.setIdVoucher(null);
            phanThuong.setGiaTriXu(0);
        }

        return phanThuongMinigameRepository.save(phanThuong);
    }

    // === ADMIN: Cập nhật phần thưởng ===
    @Override
    @Transactional
    public PhanThuongMinigame updatePhanThuong(Integer id, PhanThuongMinigame phanThuongMoi) {
        PhanThuongMinigame existing = phanThuongMinigameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phần thưởng có ID: " + id));

        existing.setTenPhanThuong(phanThuongMoi.getTenPhanThuong());
        existing.setLoaiGame(phanThuongMoi.getLoaiGame());
        existing.setLoaiPhanThuong(phanThuongMoi.getLoaiPhanThuong());
        existing.setTyLeTrung(phanThuongMoi.getTyLeTrung());
        existing.setSoLuongGioiHan(phanThuongMoi.getSoLuongGioiHan());
        existing.setTrangThai(phanThuongMoi.getTrangThai());

        // Xử lý logic gán dữ liệu chuẩn xác theo loại phần thưởng
        if ("voucher".equals(phanThuongMoi.getLoaiPhanThuong())) {
            existing.setIdVoucher(phanThuongMoi.getIdVoucher()); // Nhận ID voucher từ Vue 3 gửi lên
            existing.setGiaTriXu(0);
        } else if ("xu".equals(phanThuongMoi.getLoaiPhanThuong())) {
            existing.setGiaTriXu(phanThuongMoi.getGiaTriXu());
            existing.setIdVoucher(null); // Xóa id_voucher nếu đổi sang nhận xu
        } else {
            existing.setIdVoucher(null);
            existing.setGiaTriXu(0);
        }

        return phanThuongMinigameRepository.save(existing);
    }

    // === ADMIN: Xóa / Vô hiệu hóa phần thưởng ===
    @Override
    @Transactional
    public void deletePhanThuong(Integer id) {
        if (!phanThuongMinigameRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phần thưởng để xóa!");
        }
        phanThuongMinigameRepository.deleteById(id);
    }

    // === ADMIN: Xem toàn bộ lịch sử biến động xu hệ thống ===
    @Override
    public List<LichSuXu> getAllLichSuXuSystem() {
        return lichSuXuRepository.findAll();
    }

    @Override
    @Transactional
    public VoucherCuaKhachHang doiXuLayVoucher(Integer idKhachHang, Integer idKhoVoucher) {
        // 1. Lấy thông tin khách hàng và voucher
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        KhoVoucher voucher = khoVoucherRepository.findById(idKhoVoucher)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại!"));

        // 2. Kiểm tra số lượng voucher trong kho (tránh null)
        int soLuongConLai = voucher.getSoLuongConLai() != null ? voucher.getSoLuongConLai() : 0;
        if (soLuongConLai <= 0) {
            throw new RuntimeException("Voucher đã hết lượt đổi!");
        }

        // 3. Kiểm tra số dư xu của khách (tránh null)
        int soDuTruoc = khachHang.getSoDuXu() != null ? khachHang.getSoDuXu() : 0;
        int soXuDoi = voucher.getSoXuDoi() != null ? voucher.getSoXuDoi() : 0;

        if (soDuTruoc < soXuDoi) {
            throw new RuntimeException("Số dư xu của bạn không đủ để đổi voucher này!");
        }

        // 4. Trừ xu của khách
        khachHang.setSoDuXu(soDuTruoc - soXuDoi);
        khachHangRepository.save(khachHang);

        // 5. Trừ số lượng voucher trong kho
        voucher.setSoLuongConLai(soLuongConLai - 1);
        khoVoucherRepository.save(voucher);

        // 6. Lưu vào kho voucher cá nhân của khách (Dùng setIdKhoVoucher khớp với Entity)
        VoucherCuaKhachHang vckh = new VoucherCuaKhachHang();
        vckh.setIdKhachHang(idKhachHang);
        vckh.setIdKhoVoucher(idKhoVoucher);
        vckh.setTrangThai("CHUA_DUNG");
        vckh.setNgayDoi(LocalDateTime.now());
        VoucherCuaKhachHang savedVoucher = voucherCuaKhachHangRepository.save(vckh);

        // 7. Ghi lại lịch sử biến động xu
        LichSuXu lichSu = new LichSuXu();
        lichSu.setIdKhachHang(khachHang);
        lichSu.setSoXuThayDoi(-soXuDoi);
        lichSu.setSoDuTruoc(soDuTruoc);
        lichSu.setSoDuSau(khachHang.getSoDuXu());
        lichSu.setLoaiGiaoDich("doi_voucher");
        lichSu.setMoTa("Đổi xu lấy voucher: " + voucher.getTenVoucher());
        lichSu.setNgayTao(LocalDateTime.now());
        lichSuXuRepository.save(lichSu);

        return savedVoucher;
    }

    @Override
    public List<KhoVoucher> getAllKhoVoucher() {
        return khoVoucherRepository.findAll();
    }

    @Override
    public List<VoucherCuaKhachHangResponse> getVoucherCuaKhachHang(Integer idKhachHang) {

        List<Object[]> list = voucherCuaKhachHangRepository.getVoucherResponse(idKhachHang);

        List<VoucherCuaKhachHangResponse> result = new ArrayList<>();

        for (Object[] row : list) {

            VoucherCuaKhachHangResponse dto = new VoucherCuaKhachHangResponse();

            dto.setIdVoucherKhachHang((Integer) row[0]);
            dto.setTrangThai((String) row[1]);
            dto.setNgayDoi((LocalDateTime) row[2]);


            dto.setIdKhoVoucher((Integer) row[3]);
            dto.setTenVoucher((String) row[4]);
            dto.setMaCode((String) row[5]);

            dto.setLoaiGiamGia((String) row[6]);
            dto.setGiaTriGiam((BigDecimal) row[7]);
            dto.setGiaTriGiamToiDa((BigDecimal) row[8]);
            dto.setDieuKienToiThieu((BigDecimal) row[9]);
            dto.setSoXuDoi((Integer) row[10]);

            dto.setNgayBatDau((LocalDateTime) row[11]);
            dto.setNgayHetHan((LocalDateTime) row[12]);

            dto.setMoTa((String) row[13]);
            // ====================
// Xử lý dữ liệu hiển thị
// ====================

            LocalDateTime now = LocalDateTime.now();

// Hiển thị giá trị giảm
            if ("phan_tram".equalsIgnoreCase(dto.getLoaiGiamGia())) {

                String hienThi = "Giảm "
                        + dto.getGiaTriGiam().stripTrailingZeros().toPlainString()
                        + "%";

                if (dto.getGiaTriGiamToiDa() != null) {
                    hienThi += " (tối đa "
                            + dto.getGiaTriGiamToiDa().stripTrailingZeros().toPlainString()
                            + "đ)";
                }

                dto.setHienThiGiaTriGiam(hienThi);

            } else {

                dto.setHienThiGiaTriGiam(
                        "Giảm "
                                + dto.getGiaTriGiam().stripTrailingZeros().toPlainString()
                                + "đ");
            }

// Còn bao nhiêu ngày
            long soNgay = java.time.temporal.ChronoUnit.DAYS.between(
                    now.toLocalDate(),
                    dto.getNgayHetHan().toLocalDate()
            );

            if (soNgay < 0) {
                soNgay = 0;
            }

            dto.setSoNgayConLai(soNgay);

// Sắp hết hạn nếu còn <= 3 ngày
            dto.setSapHetHan(soNgay <= 3);


            result.add(dto);
        }

        return result;
    }

    // === ADMIN: Thêm mẫu kho voucher mới ===
    @Override
    @Transactional
    public KhoVoucher createKhoVoucher(KhoVoucher khoVoucher) {

        if (khoVoucher.getTrangThai() == null) {
            khoVoucher.setTrangThai(true);
        }

        khoVoucher.setMaCode(generateMaCode());

        return khoVoucherRepository.save(khoVoucher);
    }

    @Override
    @Transactional
    public KhoVoucher updateKhoVoucher(Integer id, KhoVoucher khoVoucherMoi) {
        KhoVoucher existing = khoVoucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kho voucher có ID: " + id));

        existing.setTenVoucher(khoVoucherMoi.getTenVoucher());
        existing.setGiaTriGiam(khoVoucherMoi.getGiaTriGiam());
        existing.setGiaTriGiamToiDa(khoVoucherMoi.getGiaTriGiamToiDa());
        existing.setDieuKienToiThieu(khoVoucherMoi.getDieuKienToiThieu());
        existing.setSoXuDoi(khoVoucherMoi.getSoXuDoi());
        existing.setSoLuongConLai(khoVoucherMoi.getSoLuongConLai());
        existing.setNgayHetHan(khoVoucherMoi.getNgayHetHan());
        existing.setTrangThai(khoVoucherMoi.getTrangThai());


        KhoVoucher updated = khoVoucherRepository.save(existing);

// cập nhật các hóa đơn đang dùng kho voucher
        hoaDonService.capNhatHoaDonTheoKhoVoucher(updated.getId());

// bắn socket
        posSocketService.send(
                new PosEvent(
                        "KHO_VOUCHER_UPDATED",
                        null,
                        updated.getId(),
                        null
                )
        );

        return updated;
    }

    // === ADMIN: Xóa / Vô hiệu hóa kho voucher ===
    @Override
    @Transactional
    public void deleteKhoVoucher(Integer id) {
        if (!khoVoucherRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy kho voucher để xóa!");
        }
        khoVoucherRepository.deleteById(id);
    }

    private String generateMaCode() {
        return "KHO-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
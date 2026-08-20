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
import java.util.stream.Collectors;


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
    private CauHinhDiemDanhRepository cauHinhDiemDanhRepository;

    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private PosSocketService posSocketService;

    @Override
    @Transactional
    public DiemDanhHangNgay diemDanhHangNgay(Integer idInput) {
        // 1. Tìm khách hàng: ưu tiên tìm theo id_tai_khoan, nếu không thấy thử tìm theo id khách hàng
        KhachHang khachHang = khachHangRepository.findByIdTaiKhoan_Id(idInput)
                .orElseGet(() -> khachHangRepository.findById(idInput)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin khách hàng!"))
                );

        Integer idKhachHangThucTe = khachHang.getId(); // Lấy ID chuẩn trong bảng khach_hang
        LocalDate today = LocalDate.now();

        // 2. Kiểm tra hôm nay đã điểm danh chưa (dùng ID chuẩn thực tế)
        boolean daDiemDanh = diemDanhHangNgayRepository.existsByIdKhachHang_IdAndNgayDiemDanh(idKhachHangThucTe, today);
        if (daDiemDanh) {
            throw new RuntimeException("Hôm nay bạn đã điểm danh rồi!");
        }

        // 3. Tính toán chuỗi Streak
        LocalDate yesterday = today.minusDays(1);
        int chuoiMoi = 1;

        if (khachHang.getNgayDiemDanhGanNhat() != null && khachHang.getNgayDiemDanhGanNhat().equals(yesterday)) {
            chuoiMoi = khachHang.getChuoiDiemDanh() + 1;
        } else {
            chuoiMoi = 1;
        }

        // 4. LẤY SỐ XU TỰ ĐỘNG TỪ BẢNG CẤU HÌNH (Chỉ lấy các mốc đang hoạt động / trangThai = true)
        List<CauHinhDiemDanh> activeConfigs = cauHinhDiemDanhRepository.findByTrangThaiTrueOrderByNgayThuAsc();

        CauHinhDiemDanh config;
        if (activeConfigs.isEmpty()) {
            CauHinhDiemDanh defaultConfig = new CauHinhDiemDanh();
            defaultConfig.setSoXuThuong(10); // Fallback nếu chưa cấu hình hoặc toàn bộ bị khóa
            config = defaultConfig;
        } else {
            int totalConfigDays = activeConfigs.size(); // Tổng số ngày đang mở hoạt động

            // Công thức vòng lặp modular dựa trên danh sách thực tế đang hoạt động
            int targetIndex = (int) ((chuoiMoi - 1) % totalConfigDays);
            config = activeConfigs.get(targetIndex);
        }

        int soXuNhan = config.getSoXuThuong() != null ? config.getSoXuThuong() : 10;

        // 5. Cập nhật thông tin Khách hàng
        int soDuTruoc = khachHang.getSoDuXu() != null ? khachHang.getSoDuXu() : 0;
        khachHang.setSoDuXu(soDuTruoc + soXuNhan);
        khachHang.setChuoiDiemDanh(chuoiMoi);
        khachHang.setNgayDiemDanhGanNhat(today);
        khachHangRepository.save(khachHang);

        // 6. Lưu lịch sử điểm danh
        DiemDanhHangNgay diemDanh = new DiemDanhHangNgay();
        diemDanh.setIdKhachHang(khachHang);
        diemDanh.setNgayDiemDanh(today);
        diemDanh.setSoXuNhan(soXuNhan);
        diemDanh.setSoNgayLienTiep(chuoiMoi);
        diemDanhHangNgayRepository.save(diemDanh);

        // 7. Ghi log biến động ví xu
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
    public PhanThuongMinigame quayThuong(Integer idInput, String loaiGame) {
        // 1. Giải mã idInput thành khách hàng thực tế (Tránh lỗi lệch id_tai_khoan và id_khach_hang)
        KhachHang khachHang = khachHangRepository.findByIdTaiKhoan_Id(idInput)
                .orElseGet(() -> khachHangRepository.findById(idInput)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"))
                );

        Integer idKhachHangThucTe = khachHang.getId();
        int soDuTruoc = khachHang.getSoDuXu() != null ? khachHang.getSoDuXu() : 0;

        // 2. Xử lý trừ phí nếu là Vòng quay (Mất 5 xu). Nếu là lật thẻ sẽ tự động bỏ qua (miễn phí)
        if ("vong_quay".equals(loaiGame)) {
            if (soDuTruoc < 5) {
                throw new RuntimeException("Số dư xu của bạn không đủ (cần 5 Xu) để quay thưởng!");
            }
            khachHang.setSoDuXu(soDuTruoc - 5);

            // Ghi log biến động xu (trừ phí quay)
            LichSuXu logPhi = new LichSuXu();
            logPhi.setIdKhachHang(khachHang);
            logPhi.setSoXuThayDoi(-5);
            logPhi.setSoDuTruoc(soDuTruoc);
            logPhi.setSoDuSau(khachHang.getSoDuXu());
            logPhi.setLoaiGiaoDich("phi_vong_quay");
            logPhi.setMoTa("Tham gia Vòng quay may mắn");
            logPhi.setNgayTao(LocalDateTime.now());
            lichSuXuRepository.save(logPhi);

            soDuTruoc = khachHang.getSoDuXu(); // Cập nhật lại số dư sau khi trừ phí
        }

        // 3. Lấy danh sách phần thưởng hợp lệ (Trạng thái hoạt động và chưa vượt giới hạn phát hành)
        List<PhanThuongMinigame> dsPhanThuong = phanThuongMinigameRepository.findByLoaiGameAndTrangThai(loaiGame, true);

        dsPhanThuong = dsPhanThuong.stream().filter(p ->
                p.getSoLuongGioiHan() == null || p.getSoLuongGioiHan() == -1 || p.getSoLuongDaTrung() < p.getSoLuongGioiHan()
        ).collect(Collectors.toList());

        if (dsPhanThuong.isEmpty()) {
            throw new RuntimeException("Minigame hiện tại đã hết phần thưởng phát hành!");
        }

        // 4. Thuật toán quay ngẫu nhiên dựa theo tỷ lệ trúng (ty_le_trung)
        Random random = new Random();
        double randomVal = random.nextDouble() * 100;

        double currentSum = 0;
        PhanThuongMinigame phanThuongTrung = dsPhanThuong.get(0);

        for (PhanThuongMinigame pt : dsPhanThuong) {
            currentSum += pt.getTyLeTrung().doubleValue();
            if (randomVal <= currentSum) {
                phanThuongTrung = pt;
                break;
            }
        }

        // 5. Tăng số lượng đã trúng của phần thưởng này lên 1
        int daTrungCu = phanThuongTrung.getSoLuongDaTrung() != null ? phanThuongTrung.getSoLuongDaTrung() : 0;
        phanThuongTrung.setSoLuongDaTrung(daTrungCu + 1);
        phanThuongMinigameRepository.save(phanThuongTrung);

        // 6. Xử lý phần thưởng nhận được (XU hoặc VOUCHER)
        if ("xu".equals(phanThuongTrung.getLoaiPhanThuong())) {
            int soXuThuong = phanThuongTrung.getGiaTriXu() != null ? phanThuongTrung.getGiaTriXu() : 0;

            khachHang.setSoDuXu(soDuTruoc + soXuThuong);
            khachHangRepository.save(khachHang);

            // Phân tách chi tiết loại giao dịch và mô tả riêng cho từng trò chơi
            String loaiGiaoDich;
            String moTaChiTiet;

            if ("vong_quay".equals(loaiGame)) {
                loaiGiaoDich = "trung_vong_quay";
                moTaChiTiet = "Trúng phần thưởng vòng quay: " + phanThuongTrung.getTenPhanThuong();
            } else if ("lat_the".equals(loaiGame)) {
                loaiGiaoDich = "trung_lat_the";
                moTaChiTiet = "Trúng phần thưởng lật thẻ: " + phanThuongTrung.getTenPhanThuong();
            } else {
                loaiGiaoDich = "trung_minigame";
                moTaChiTiet = "Trúng phần thưởng minigame: " + phanThuongTrung.getTenPhanThuong();
            }

            // Ghi log biến động xu (cộng thưởng)
            LichSuXu logXu = new LichSuXu();
            logXu.setIdKhachHang(khachHang);
            logXu.setSoXuThayDoi(soXuThuong);
            logXu.setSoDuTruoc(soDuTruoc);
            logXu.setSoDuSau(khachHang.getSoDuXu());
            logXu.setLoaiGiaoDich(loaiGiaoDich);
            logXu.setMoTa(moTaChiTiet);
            logXu.setNgayTao(LocalDateTime.now());
            lichSuXuRepository.save(logXu);

        } else if ("voucher".equals(phanThuongTrung.getLoaiPhanThuong())) {
            if (phanThuongTrung.getIdVoucher() != null) {
                // Lấy thông tin voucher từ kho (bảng kho_voucher)
                KhoVoucher voucher = khoVoucherRepository.findById(phanThuongTrung.getIdVoucher())
                        .orElseThrow(() -> new RuntimeException("Voucher phần thưởng không tồn tại trong kho!"));

                int soLuongConLai = voucher.getSoLuongConLai() != null ? voucher.getSoLuongConLai() : 0;
                if (soLuongConLai <= 0) {
                    throw new RuntimeException("Rất tiếc, mã voucher phần thưởng này đã hết lượt trong kho!");
                }

                // Trừ số lượng tồn kho của voucher
                voucher.setSoLuongConLai(soLuongConLai - 1);
                khoVoucherRepository.save(voucher);

                // INSERT VÀO VÍ VOUCHER CỦA KHÁCH HÀNG
                VoucherCuaKhachHang vckh = new VoucherCuaKhachHang();
                vckh.setIdKhachHang(idKhachHangThucTe);
                vckh.setIdVoucherMinigame(phanThuongTrung.getIdVoucher());
                vckh.setTrangThai("CHUA_DUNG");
                vckh.setNgayDoi(LocalDateTime.now());
                voucherCuaKhachHangRepository.save(vckh);
            }
        }

        // 7. Lưu lại lịch sử tham gia minigame của khách
        LichSuMinigame lichSuGame = new LichSuMinigame();
        lichSuGame.setIdKhachHang(khachHang);
        lichSuGame.setIdPhanThuong(phanThuongTrung);
        lichSuGame.setLoaiGame(loaiGame);
        lichSuGame.setTrangThaiNhan("da_nhan");
        lichSuGame.setNgayTao(LocalDateTime.now());
        lichSuMinigameRepository.save(lichSuGame);

        return phanThuongTrung;
    }

    @Override
    public TrangThaiGamificationResponse getTrangThaiGamification(Integer idTaiKhoan) {
        KhachHang khachHang = khachHangRepository.findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin khách hàng cho tài khoản này!"));

        LocalDate today = LocalDate.now();
        Integer idKhachHangThucTe = khachHang.getId();

        boolean daDiemDanh = diemDanhHangNgayRepository.existsByIdKhachHang_IdAndNgayDiemDanh(idKhachHangThucTe, today);

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);
        long soLuotDaChoi = lichSuMinigameRepository.countByIdKhachHang_IdAndLoaiGameAndNgayTaoBetween(
                idKhachHangThucTe, "lat_the", startOfDay, endOfDay
        );

        int gioiHanToiDa = khachHang.getSoLuotLatThe() != null ? khachHang.getSoLuotLatThe() : 3;
        int soLuotLatTheConLai = Math.max(0, gioiHanToiDa - (int) soLuotDaChoi);

        return new TrangThaiGamificationResponse(
                khachHang.getId(),
                khachHang.getSoDuXu() != null ? khachHang.getSoDuXu() : 0,
                khachHang.getChuoiDiemDanh() != null ? khachHang.getChuoiDiemDanh() : 0,
                khachHang.getNgayDiemDanhGanNhat(),
                daDiemDanh,
                soLuotLatTheConLai,
                gioiHanToiDa
        );
    }

    // === 3. BỔ SUNG HÀM CHO ADMIN: Cập nhật lượt lật thẻ cho cá nhân ===
    @Override
    @Transactional
    public void updateSoLuotLatTheChoKhachHang(Integer idKhachHang, Integer soLuotMoi) {
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng có ID: " + idKhachHang));
        khachHang.setSoLuotLatThe(soLuotMoi);
        khachHangRepository.save(khachHang);
    }

    // === 4. BỔ SUNG HÀM CHO ADMIN: Cập nhật lượt lật thẻ cho toàn bộ hệ thống ===
    @Override
    @Transactional
    public void updateSoLuotLatTheChoToanBo(Integer soLuotMoi) {
        List<KhachHang> listKhachHang = khachHangRepository.findAll();
        for (KhachHang kh : listKhachHang) {
            kh.setSoLuotLatThe(soLuotMoi);
        }
        khachHangRepository.saveAll(listKhachHang);
    }

    @Override
    public List<LichSuXuResponse> getLichSuXu(Integer idInput) {
        // Fix: Lấy id khách hàng thực tế từ id tài khoản
        KhachHang khachHang = khachHangRepository.findByIdTaiKhoan_Id(idInput)
                .orElseGet(() -> khachHangRepository.findById(idInput)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"))
                );

        List<LichSuXu> list = lichSuXuRepository.findByIdKhachHang_IdOrderByNgayTaoDesc(khachHang.getId());

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

    // === ADMIN: Thêm mới phần thưởng ===
    @Override
    @Transactional
    public PhanThuongMinigame createPhanThuong(PhanThuongMinigame phanThuong) {
        // 🛡 Ép id về null để chắc chắn tạo bản ghi mới (INSERT),
        // tránh việc dính id cũ dẫn đến UPDATE ghi đè vào 7 bản ghi có sẵn.
        phanThuong.setId(null);

        if ("voucher".equals(phanThuong.getLoaiPhanThuong())) {
            phanThuong.setGiaTriXu(0);
        } else if ("xu".equals(phanThuong.getLoaiPhanThuong())) {
            phanThuong.setIdVoucher(null);
        } else {
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

        if ("voucher".equals(phanThuongMoi.getLoaiPhanThuong())) {
            existing.setIdVoucher(phanThuongMoi.getIdVoucher());
            existing.setGiaTriXu(0);
        } else if ("xu".equals(phanThuongMoi.getLoaiPhanThuong())) {
            existing.setGiaTriXu(phanThuongMoi.getGiaTriXu());
            existing.setIdVoucher(null);
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
    public VoucherCuaKhachHang doiXuLayVoucher(Integer idInput, Integer idKhoVoucher) {
        // Fix: Giải mã idInput thành khách hàng thực tế
        KhachHang khachHang = khachHangRepository.findByIdTaiKhoan_Id(idInput)
                .orElseGet(() -> khachHangRepository.findById(idInput)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"))
                );

        KhoVoucher voucher = khoVoucherRepository.findById(idKhoVoucher)
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại!"));

        int soLuongConLai = voucher.getSoLuongConLai() != null ? voucher.getSoLuongConLai() : 0;
        if (soLuongConLai <= 0) {
            throw new RuntimeException("Voucher đã hết lượt đổi!");
        }

        int soDuTruoc = khachHang.getSoDuXu() != null ? khachHang.getSoDuXu() : 0;
        int soXuDoi = voucher.getSoXuDoi() != null ? voucher.getSoXuDoi() : 0;

        if (soDuTruoc < soXuDoi) {
            throw new RuntimeException("Số dư xu của bạn không đủ để đổi voucher này!");
        }

        khachHang.setSoDuXu(soDuTruoc - soXuDoi);
        khachHangRepository.save(khachHang);

        voucher.setSoLuongConLai(soLuongConLai - 1);
        khoVoucherRepository.save(voucher);

        VoucherCuaKhachHang vckh = new VoucherCuaKhachHang();
        vckh.setIdKhachHang(khachHang.getId()); // Dùng ID chuẩn của khách hàng
        vckh.setIdVoucherMinigame(idKhoVoucher);
        vckh.setTrangThai("CHUA_DUNG");
        vckh.setNgayDoi(LocalDateTime.now());
        VoucherCuaKhachHang savedVoucher = voucherCuaKhachHangRepository.save(vckh);

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
    public List<VoucherCuaKhachHangResponse> getVoucherCuaKhachHang(Integer idInput) {
        // Fix: Giải mã idInput thành khách hàng thực tế
        KhachHang khachHang = khachHangRepository.findByIdTaiKhoan_Id(idInput)
                .orElseGet(() -> khachHangRepository.findById(idInput)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"))
                );

        List<Object[]> list = voucherCuaKhachHangRepository.getVoucherResponse(khachHang.getId());

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

            LocalDateTime now = LocalDateTime.now();

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

            long soNgay = java.time.temporal.ChronoUnit.DAYS.between(
                    now.toLocalDate(),
                    dto.getNgayHetHan().toLocalDate()
            );

            if (soNgay < 0) {
                soNgay = 0;
            }

            dto.setSoNgayConLai(soNgay);
            dto.setSapHetHan(soNgay <= 3);

            result.add(dto);
        }

        return result;
    }

    @Override
    @Transactional
    public KhoVoucher createKhoVoucher(KhoVoucher khoVoucher) {
        if (khoVoucher.getTrangThai() == null) {
            khoVoucher.setTrangThai(true);
        }

        // Set số lượng lớn để giả lập vô hạn tạm thời
        khoVoucher.setSoLuongConLai(999999);

        khoVoucher.setMaCode(generateMaCode());
        KhoVoucher saved = khoVoucherRepository.save(khoVoucher);

        // Gửi Socket báo thêm mới
        posSocketService.send(
                new PosEvent(
                        "KHO_VOUCHER_CREATED",
                        null,
                        saved.getId(),
                        null
                )
        );

        return saved;
    }

    @Override
    @Transactional
    public KhoVoucher updateKhoVoucher(Integer id, KhoVoucher khoVoucherMoi) {

        KhoVoucher existing = khoVoucherRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy kho voucher có ID: " + id));

        existing.setTenVoucher(khoVoucherMoi.getTenVoucher());
        existing.setGiaTriGiam(khoVoucherMoi.getGiaTriGiam());
        existing.setGiaTriGiamToiDa(khoVoucherMoi.getGiaTriGiamToiDa());
        existing.setDieuKienToiThieu(khoVoucherMoi.getDieuKienToiThieu());
        existing.setSoXuDoi(khoVoucherMoi.getSoXuDoi());
        existing.setSoLuongConLai(999999);
        existing.setNgayHetHan(khoVoucherMoi.getNgayHetHan());
        existing.setLoaiGiamGia(khoVoucherMoi.getLoaiGiamGia());

        // Nếu ngày hết hạn đã qua thì tự động chuyển sang ngừng
        if (khoVoucherMoi.getNgayHetHan() != null
                && khoVoucherMoi.getNgayHetHan().isBefore(LocalDateTime.now())) {

            existing.setTrangThai(false);

        } else {
            existing.setTrangThai(khoVoucherMoi.getTrangThai());
        }

        KhoVoucher updated = khoVoucherRepository.save(existing);

        hoaDonService.capNhatHoaDonTheoKhoVoucher(updated.getId());

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

    @Override
    @Transactional
    public void deleteKhoVoucher(Integer id) {
        if (!khoVoucherRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy kho voucher để xóa!");
        }
        khoVoucherRepository.deleteById(id);

        // Gửi Socket báo xóa
        posSocketService.send(
                new PosEvent(
                        "KHO_VOUCHER_DELETED",
                        null,
                        id,
                        null
                )
        );
    }

    private String generateMaCode() {
        return "KHO-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
package com.example.backend.Service;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Request.SanPhamRequest;
import com.example.backend.Response.SanPhamChiTietResponse;
import com.example.backend.Response.SanPhamResponse;
import com.example.backend.websocket.PosAlertEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private PosSocketService posSocketService;

    public SanPham create(SanPhamRequest req) {

        DanhMuc danhMuc = danhMucRepository.findById(req.getIdDanhMuc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        ThuongHieu thuongHieu = thuongHieuRepository.findById(req.getIdThuongHieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));

        ChatLieu chatLieu = chatLieuRepository.findById(req.getIdChatLieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu"));

        SanPham sp = new SanPham();

        sp.setIdDanhMuc(danhMuc);
        sp.setIdThuongHieu(thuongHieu);
        sp.setIdChatLieu(chatLieu);

        sp.setTenSanPham(req.getTenSanPham());
        sp.setTrangThai(req.getTrangThai());
        sp.setMoTa(req.getMoTa());

        // Lưu lần đầu để sinh ID
        sp = sanPhamRepository.save(sp);

        // Sinh mã từ ID
        sp.setMaSanPham(String.format("SP%06d", sp.getId()));

        // Cập nhật lại mã
        return sanPhamRepository.save(sp);
    }

    // ================= UPDATE =================
    @Transactional
    public SanPham update(Integer id, SanPhamRequest req) {

        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        DanhMuc danhMuc = danhMucRepository.findById(req.getIdDanhMuc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        ThuongHieu thuongHieu = thuongHieuRepository.findById(req.getIdThuongHieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));

        ChatLieu chatLieu = chatLieuRepository.findById(req.getIdChatLieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu"));

        sp.setIdDanhMuc(danhMuc);
        sp.setIdThuongHieu(thuongHieu);
        sp.setIdChatLieu(chatLieu);
        sp.setTrangThai(req.getTrangThai());
        sp.setTenSanPham(req.getTenSanPham());
        sp.setMoTa(req.getMoTa());

        // ================== XỬ LÝ KHI CHUYỂN SANG NGỪNG KINH DOANH ==================
        boolean isNgungKinhDoanh = Boolean.FALSE.equals(req.getTrangThai());

        if (isNgungKinhDoanh) {
            xuLyNgungKinhDoanhSanPham(sp);
        }

        return sanPhamRepository.save(sp);
    }

    // ================= DELETE =================
    @Transactional
    public void delete(Integer id) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Xóa mềm: Chuyển trạng thái sản phẩm cha về false
        sp.setTrangThai(false);
        sanPhamRepository.save(sp);

        // Tắt toàn bộ biến thể con + gỡ khỏi hóa đơn chờ tại quầy + tính lại tồn giữ + bắn Socket
        xuLyNgungKinhDoanhSanPham(sp);
    }

    // ================== HELPER METHOD TÁCH DÙNG CHUNG ==================
    private void xuLyNgungKinhDoanhSanPham(SanPham sp) {
        List<SanPhamChiTiet> dsSpct = sanPhamChiTietRepository.findByIdSanPham_Id(sp.getId());

        if (dsSpct.isEmpty()) {
            return;
        }

        Set<HoaDon> dsHoaDonCanCapNhat = new HashSet<>();
        List<String> trangThaiKhongThuongLuong = List.of("da_xac_nhan", "cho_van_chuyen", "dang_giao");

        for (SanPhamChiTiet spct : dsSpct) {
            // 1. Tắt trạng thái biến thể con
            spct.setTrangThai(false);

            // 2. CHỈ xóa SPCT khỏi Hóa Đơn Chờ TẠI QUẦY (TAI_QUAY)
            // 🔴 Đơn Online chờ xác nhận GIỮ NGUYÊN không chạm vào
            List<HoaDonChiTiet> dsHdctChoTaiQuay = hoaDonChiTietRepository
                    .findByIdSanPhamChiTiet_IdAndIdHoaDon_TrangThaiAndIdHoaDon_LoaiHoaDon(
                            spct.getId(), "cho_xac_nhan", "TAI_QUAY");

            for (HoaDonChiTiet ct : dsHdctChoTaiQuay) {
                dsHoaDonCanCapNhat.add(ct.getIdHoaDon());
                hoaDonChiTietRepository.delete(ct);
            }

            // 3. Tính lại Số lượng tạm giữ thực tế cho từng SPCT con
            Integer soLuongDaXacNhanHoacGiao = hoaDonChiTietRepository
                    .sumSoLuongBySpctAndTrangThaiIn(spct.getId(), trangThaiKhongThuongLuong);
            if (soLuongDaXacNhanHoacGiao == null) soLuongDaXacNhanHoacGiao = 0;

            Integer soLuongChoOnline = hoaDonChiTietRepository
                    .sumSoLuongBySpctAndTrangThaiAndLoaiHoaDon(spct.getId(), "cho_xac_nhan", "ONLINE");
            if (soLuongChoOnline == null) soLuongChoOnline = 0;

            // Cập nhật số lượng tạm giữ khớp với các đơn Online & đơn đang giao còn lại
            spct.setSoLuongTamGiu(soLuongDaXacNhanHoacGiao + soLuongChoOnline);
        }

        // Lưu cập nhật danh sách các biến thể con
        sanPhamChiTietRepository.saveAll(dsSpct);

        // Ép xóa khỏi DB ngay lập tức trước khi tính lại tiền hóa đơn
        hoaDonChiTietRepository.flush();

        // 4. Tính toán lại tổng tiền cho các Hóa đơn chờ TẠI QUẦY bị ảnh hưởng
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

        // 5. Bắn Socket thông báo sang màn hình Thu ngân (POS)
        final String thongBao = "Sản phẩm \"" + sp.getTenSanPham()
                + "\" đã ngừng kinh doanh. Đã tự động gỡ toàn bộ biến thể khỏi các hóa đơn chờ tại quầy!";

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                try {
                    posSocketService.send(
                            new PosAlertEvent("STOCK_FORCE_ADJUSTED", thongBao, null, 0)
                    );
                } catch (Exception e) {
                    System.err.println("Lỗi gửi Socket: " + e.getMessage());
                }
            }
        });
    }
    // ================= GET =================
    public List<SanPhamResponse> getAll() {
        return sanPhamChiTietService.getAllSanPham();
    }

    public SanPham getById(Integer id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }


}
package com.example.backend.Service.impl;

import com.example.backend.Entity.DiaChiKhachHang;
import com.example.backend.Entity.KhachHang;
import com.example.backend.Entity.TaiKhoan;
import com.example.backend.Entity.VaiTro;
import com.example.backend.Repository.DiaChiKhachHangRepository;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.Repository.TaiKhoanRepository;
import com.example.backend.Repository.VaiTroRepository;
import com.example.backend.Request.KhachHangRequest;
import com.example.backend.Response.KhachHangResponse;
import com.example.backend.Service.KhachHangService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.time.Instant;
import java.util.List;

@Service
public class KhachHangImpl implements KhachHangService {
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private DiaChiKhachHangRepository diaChiKhachHangRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;


    @Override
    public List<KhachHang> getKhachHang() {
        return khachHangRepository.findAll();
    }

    @Override
    public Page<KhachHangResponse> phanTrangKhachHang(Integer pageNo, Integer pageSize) {
        int currentPage = pageNo == null || pageNo < 0 ? 0 : pageNo;
        int currentSize = pageSize == null || pageSize <= 0 ? 5 : pageSize;
        return khachHangRepository.phanTrang(PageRequest.of(currentPage, currentSize));
    }

    @Override
    @Transactional
    public void addKhachHang(KhachHangRequest request, MultipartFile file) {
        validateRequest(request, null);

        VaiTro vaiTro = vaiTroRepository.findById(3)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò khách hàng"));

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setIdVaiTro(vaiTro);
        taiKhoan.setTenTaiKhoan(request.getTenTaiKhoan().trim());
        taiKhoan.setEmail(request.getEmail().trim());
        taiKhoan.setSoDienThoai(request.getSoDienThoai().trim());
        taiKhoan.setMatKhau("123456");
        taiKhoan.setNgayTao(Instant.now());
        taiKhoan.setNgayCapNhat(Instant.now());
        taiKhoan.setTrangThai(1);

        taiKhoan = taiKhoanRepository.save(taiKhoan);

        KhachHang khachHang = new KhachHang();
        khachHang.setIdTaiKhoan(taiKhoan);
        khachHang.setMaKhachHang(isBlank(request.getMaKhachHang()) ? generateMaKhachHang() : request.getMaKhachHang().trim());
        khachHang.setHoTen(request.getHoTen().trim());
        khachHang.setSoDienThoai(request.getSoDienThoai().trim());

        // Gộp chuỗi địa chỉ: dia_chi_cu_the + phuong + quan + thanh_pho đưa vào bảng khách hàng
        khachHang.setDiaChi(formatFullAddress(request));

        if (request.getNgaySinh() != null) {
            java.time.LocalDate localDateNgaySinh = request.getNgaySinh().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
            khachHang.setNgaySinh(localDateNgaySinh);
        } else {
            khachHang.setNgaySinh(null);
        }
        khachHang.setGioiTinh("Nam".equalsIgnoreCase(request.getGioiTinh()) || "true".equalsIgnoreCase(request.getGioiTinh()));

        khachHang.setTrangThai(request.getTrangThai() != null && request.getTrangThai());
        khachHang.setNgayTao(Instant.now());
        khachHang.setNgayCapNhat(Instant.now());
        if (file != null && !file.isEmpty()) {
            khachHang.setAnh(saveFile(file));
        }
        khachHang = khachHangRepository.save(khachHang);

        saveDefaultAddress(khachHang, request);
    }

    @Override
    @Transactional
    public void updateKhachHang(Integer id, KhachHangRequest request, MultipartFile file) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));
        validateRequest(request, khachHang.getIdTaiKhoan() == null ? null : khachHang.getIdTaiKhoan().getId());

        TaiKhoan taiKhoan = khachHang.getIdTaiKhoan();
        if (taiKhoan == null) {
            throw new RuntimeException("Khách hàng chưa có tài khoản");
        }
        taiKhoan.setTenTaiKhoan(request.getTenTaiKhoan().trim());
        taiKhoan.setEmail(request.getEmail().trim());
        taiKhoan.setSoDienThoai(request.getSoDienThoai().trim());
        taiKhoan.setNgayCapNhat(Instant.now());
        taiKhoanRepository.save(taiKhoan);

        if (!isBlank(request.getMaKhachHang())) {
            khachHang.setMaKhachHang(request.getMaKhachHang().trim());
        }
        khachHang.setHoTen(request.getHoTen().trim());
        khachHang.setSoDienThoai(request.getSoDienThoai().trim());

        // Cập nhật lại tổng chuỗi địa chỉ mới gộp khi chỉnh sửa thông tin khách hàng
        khachHang.setDiaChi(formatFullAddress(request));

        if (request.getNgaySinh() != null) {
            java.time.LocalDate localDateNgaySinh = request.getNgaySinh().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
            khachHang.setNgaySinh(localDateNgaySinh);
        } else {
            khachHang.setNgaySinh(null);
        }
        khachHang.setGioiTinh("Nam".equalsIgnoreCase(request.getGioiTinh()) || "true".equalsIgnoreCase(request.getGioiTinh()));

        khachHang.setTrangThai(request.getTrangThai() != null && request.getTrangThai());
        khachHang.setNgayCapNhat(Instant.now());
        if (file != null && !file.isEmpty()) {
            khachHang.setAnh(saveFile(file));
        }
        khachHangRepository.save(khachHang);
        saveDefaultAddress(khachHang, request);
    }


    @Transactional
    public void deleteKhachHang(Integer id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với ID: " + id));

        khachHang.setTrangThai(false);
        khachHang.setNgayCapNhat(Instant.now());
        khachHangRepository.save(khachHang);
    }

    @Override
    public KhachHangResponse detailKhachHang(Integer id) {
        KhachHangResponse response = khachHangRepository.detail(id);
        if (response == null) {
            throw new RuntimeException("Không tìm thấy khách hàng với ID: " + id);
        }
        return response;
    }

    @Override
    public List<KhachHangResponse> searchFullKhachHang(String keyword) {
        return khachHangRepository.search(keyword == null ? "" : keyword.trim());
    }

    @Override
    public ByteArrayInputStream khachHangExcel(String keyword, Boolean gender, Integer status) {
        List<KhachHangResponse> list = isBlank(keyword) ? phanTrangKhachHang(0, 10000).getContent() : searchFullKhachHang(keyword);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("DanhSachKhachHang");
            String[] headers = {"Mã khách hàng", "Họ tên", "Email", "Số điện thoại", "Ngày sinh", "Giới tính", "Trạng thái"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            int rowIdx = 1;
            for (KhachHangResponse item : list) {
                if (gender != null && !gender.equals(item.getGioiTinh())) continue;

                if (status != null) {
                    Boolean targetStatus = (status == 1);
                    if (!targetStatus.equals(item.getTrangThai())) continue;
                }

                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(item.getMaKhachHang());
                row.createCell(1).setCellValue(item.getHoTen());
                row.createCell(2).setCellValue(item.getEmail());
                row.createCell(3).setCellValue(item.getSoDienThoai());
                row.createCell(4).setCellValue(item.getNgaySinh() == null ? "" : item.getNgaySinh().toString());
                row.createCell(5).setCellValue(Boolean.TRUE.equals(item.getGioiTinh()) ? "Nam" : "Nữ");
                row.createCell(6).setCellValue(Boolean.TRUE.equals(item.getTrangThai()) ? "Hoạt động" : "Ngừng hoạt động");
            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi xuất file khách hàng: " + e.getMessage());
        }
    }

    private void saveDefaultAddress(KhachHang khachHang, KhachHangRequest request) {
        if (isBlank(request.getThanhPho()) && isBlank(request.getQuan()) && isBlank(request.getPhuong()) && isBlank(request.getDiaChiCuThe())) {
            return;
        }
        DiaChiKhachHang diaChi = diaChiKhachHangRepository
                .findFirstByIdKhachHang_IdAndMacDinhAndTrangThaiOrderByIdDesc(khachHang.getId(), true, 1)
                .orElseGet(DiaChiKhachHang::new);
        diaChi.setIdKhachHang(khachHang);
        if (isBlank(diaChi.getMaDiaChi())) {
            diaChi.setMaDiaChi("DC" + System.currentTimeMillis());
        }
        diaChi.setSoDienThoai(khachHang.getIdTaiKhoan().getSoDienThoai());
        diaChi.setThanhPho(defaultText(request.getThanhPho()));
        diaChi.setQuan(defaultText(request.getQuan()));
        diaChi.setPhuong(defaultText(request.getPhuong()));
        diaChi.setDiaChiCuThe(defaultText(request.getDiaChiCuThe()));
        diaChi.setMacDinh(true);
        diaChi.setTrangThai(1);
        diaChiKhachHangRepository.save(diaChi);
    }

    // Hàm tối ưu hóa logic gộp chuỗi địa chỉ thống nhất: Địa chỉ cụ thể + Phường + Quận + Thành phố
    private String formatFullAddress(KhachHangRequest request) {
        if (request == null) return null;

        StringBuilder sb = new StringBuilder();

        if (!isBlank(request.getDiaChiCuThe())) {
            sb.append(request.getDiaChiCuThe().trim());
        }
        if (!isBlank(request.getPhuong())) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(request.getPhuong().trim());
        }
        if (!isBlank(request.getQuan())) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(request.getQuan().trim());
        }
        if (!isBlank(request.getThanhPho())) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(request.getThanhPho().trim());
        }

        return sb.length() == 0 ? null : sb.toString();
    }

    private void validateRequest(KhachHangRequest request, Integer currentTaiKhoanId) {
        if (request == null) throw new RuntimeException("Dữ liệu khách hàng không được để trống");
        if (isBlank(request.getHoTen())) throw new RuntimeException("Họ tên không được để trống");
        if (isBlank(request.getTenTaiKhoan())) throw new RuntimeException("Tên tài khoản không được để trống");
        if (isBlank(request.getEmail())) throw new RuntimeException("Email không được để trống");
        if (isBlank(request.getSoDienThoai())) throw new RuntimeException("Số điện thoại không được để trống");

        taiKhoanRepository.findByEmail(request.getEmail().trim()).ifPresent(tk -> {
            if (currentTaiKhoanId == null || !tk.getId().equals(currentTaiKhoanId))
                throw new RuntimeException("Email đã tồn tại");
        });
        taiKhoanRepository.findBySoDienThoai(request.getSoDienThoai().trim()).ifPresent(tk -> {
            if (currentTaiKhoanId == null || !tk.getId().equals(currentTaiKhoanId))
                throw new RuntimeException("Số điện thoại đã tồn tại");
        });
    }

    private String saveFile(MultipartFile file) {
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/khachhang";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            file.transferTo(new File(dir, fileName));
            return fileName;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi upload file: " + e.getMessage());
        }
    }

    private String generateMaKhachHang() {
        return "KH" + System.currentTimeMillis();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String defaultText(String value) {
        return isBlank(value) ? "-" : value.trim();
    }

    @Override
    public void addKH(KhachHangRequest request) {

    }
    @Override
    public KhachHang addQuick(KhachHangRequest request) {

        KhachHang kh = new KhachHang();

        kh.setHoTen(request.getHoTen());
        kh.setSoDienThoai(request.getSoDienThoai());

        // Các giá trị mặc định
        kh.setTrangThai(true);

        return khachHangRepository.save(kh);
    }


}

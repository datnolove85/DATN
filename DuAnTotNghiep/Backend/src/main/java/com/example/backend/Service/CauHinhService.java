package com.example.backend.Service;

import com.example.backend.Entity.CauHinhHangThanhVien;
import com.example.backend.Entity.CauHinhHeThong;
import com.example.backend.Repository.CauHinhHangThanhVienRepository;
import com.example.backend.Repository.CauHinhHeThongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.text.DecimalFormat; // Nhớ import thư viện này

@Service
@RequiredArgsConstructor
public class CauHinhService {

    private final CauHinhHeThongRepository cauHinhHeThongRepository;
    private final CauHinhHangThanhVienRepository cauHinhHangThanhVienRepository;

    // ================= CẤU HÌNH HỆ THỐNG =================
    public List<CauHinhHeThong> getAllCauHinhHeThong() {
        return cauHinhHeThongRepository.findAll();
    }


    public CauHinhHeThong updateCauHinhHeThong(String maCauHinh, BigDecimal giaTriSo) {
        CauHinhHeThong config = cauHinhHeThongRepository.findByMaCauHinh(maCauHinh)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình với mã: " + maCauHinh));

        // Cập nhật giá trị số
        config.setGiaTriSo(giaTriSo);

        // Tạo mô tả chuẩn hóa cho mã cấu hình 'TI_LE_TICH_XU'
        if ("TI_LE_TICH_XU".equals(maCauHinh)) {
            DecimalFormat df = new DecimalFormat("#,###");
            String formattedValue = df.format(giaTriSo);
            config.setMoTa("Cứ " + formattedValue + "đ mua hàng được 1 xu");
        }

        // Nếu bạn có thêm các cấu hình khác, có thể dùng else if tại đây

        return cauHinhHeThongRepository.save(config);
    }

    // ================= CẤU HÌNH HẠNG THÀNH VIÊN =================
    public List<CauHinhHangThanhVien> getAllHangThanhVien() {
        return cauHinhHangThanhVienRepository.findAll();
    }

    public CauHinhHangThanhVien saveHangThanhVien(CauHinhHangThanhVien hangThanhVien) {
        // Có thể thêm logic validate: Ngưỡng chi tiêu hạng sau phải lớn hơn hạng trước
        return cauHinhHangThanhVienRepository.save(hangThanhVien);
    }

    public CauHinhHangThanhVien updateHangThanhVien(Integer id, CauHinhHangThanhVien request) {
        CauHinhHangThanhVien existing = cauHinhHangThanhVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hạng thành viên ID: " + id));

        existing.setTenHang(request.getTenHang());
        existing.setNguongChiTieuToiThieu(request.getNguongChiTieuToiThieu());
        existing.setTyLeTichXu(request.getTyLeTichXu());
        existing.setTrangThai(request.getTrangThai());

        return cauHinhHangThanhVienRepository.save(existing);
    }

    @Transactional
    public void deleteHangThanhVien(Integer id) {
        CauHinhHangThanhVien existing = cauHinhHangThanhVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hạng thành viên ID: " + id));

        // Thực hiện xóa mềm: Chuyển trạng thái thành false (Ngừng áp dụng) thay vì xóa khỏi DB
        existing.setTrangThai(false);
        cauHinhHangThanhVienRepository.save(existing);
    }
}
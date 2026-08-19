package com.example.backend.Service;

import com.example.backend.Entity.CauHinhHangThanhVien;
import com.example.backend.Entity.CauHinhHeThong;
import com.example.backend.Repository.CauHinhHangThanhVienRepository;
import com.example.backend.Repository.CauHinhHeThongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CauHinhService {

    private final CauHinhHeThongRepository cauHinhHeThongRepository;
    private final CauHinhHangThanhVienRepository cauHinhHangThanhVienRepository;

    // Đọc trực tiếp các mã cấu hình từ file application.properties (Không fix cứng nữa)
    @Value("${app.config.ma-tich-xu:TI_LE_TICH_XU}")
    private String maTichXu;

    @Value("${app.config.ma-giam-toi-da-xu:TY_LE_GIAM_TOI_DA_XU}")
    private String maGiamToiDaXu;

    @Value("${app.config.ma-quy-doi-xu:TY_LE_QUY_DOI_XU}")
    private String maQuyDoiXu;

    // ================= CẤU HÌNH HỆ THỐNG =================
    public List<CauHinhHeThong> getAllCauHinhHeThong() {
        return cauHinhHeThongRepository.findAll();
    }

    public CauHinhHeThong updateCauHinhHeThong(String maCauHinh, BigDecimal giaTriSo) {
        CauHinhHeThong config = cauHinhHeThongRepository.findByMaCauHinh(maCauHinh)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình với mã: " + maCauHinh));

        // Cập nhật giá trị số
        config.setGiaTriSo(giaTriSo);

        DecimalFormat df = new DecimalFormat("#,###");

        // So sánh bằng biến động từ properties thay vì chuỗi fix cứng
        if (maCauHinh.equals(maTichXu)) {
            String formattedValue = df.format(giaTriSo);
            config.setMoTa("Cứ " + formattedValue + "đ mua hàng được 1 xu");
        } else if (maCauHinh.equals(maGiamToiDaXu)) {
            config.setMoTa("Tối đa được dùng xu thanh toán " + giaTriSo.stripTrailingZeros().toPlainString() + "% giá trị đơn hàng");
        } else if (maCauHinh.equals(maQuyDoiXu)) {
            String formattedValue = df.format(giaTriSo);
            config.setMoTa("1 xu được quy đổi thành " + formattedValue + "đ khi thanh toán");
        }

        return cauHinhHeThongRepository.save(config);
    }

    // ================= CẤU HÌNH HẠNG THÀNH VIÊN =================
    public List<CauHinhHangThanhVien> getAllHangThanhVien() {
        return cauHinhHangThanhVienRepository.findAll();
    }

    public CauHinhHangThanhVien saveHangThanhVien(CauHinhHangThanhVien hangThanhVien) {
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

        existing.setTrangThai(false);
        cauHinhHangThanhVienRepository.save(existing);
    }
}
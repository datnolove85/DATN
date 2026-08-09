package com.example.backend.Service.impl;

import com.example.backend.Entity.CauHinhDiemDanh;
import com.example.backend.Repository.CauHinhDiemDanhRepository;
import com.example.backend.Service.CauHinhDiemDanhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class CauHinhDiemDanhServiceImpl implements CauHinhDiemDanhService {

    @Autowired
    private CauHinhDiemDanhRepository cauHinhDiemDanhRepository;

    @Override
    public List<CauHinhDiemDanh> getAllCauHinh() {
        return cauHinhDiemDanhRepository.findAllByOrderByNgayThuAsc();
    }

    @Override
    public CauHinhDiemDanh updateCauHinh(Integer id, CauHinhDiemDanh newInfo) {
        CauHinhDiemDanh config = cauHinhDiemDanhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cấu hình điểm danh với ID: " + id));

        config.setSoXuThuong(newInfo.getSoXuThuong());
        config.setTrangThai(newInfo.getTrangThai());

        return cauHinhDiemDanhRepository.save(config);
    }

    @Override
    public CauHinhDiemDanh createCauHinh(CauHinhDiemDanh config) {
        return cauHinhDiemDanhRepository.save(config);
    }

    @Override
    public void deleteCauHinh(Integer id) {
        cauHinhDiemDanhRepository.deleteById(id);
    }
    @Override
    public void generateCauHinhCapSoCong(int totalDays, int initialValue, int step) {
        // Xóa toàn bộ cấu hình cũ (hoặc giữ lại tùy nghiệp vụ của bạn)
        cauHinhDiemDanhRepository.deleteAll();

        List<CauHinhDiemDanh> listToSave = new ArrayList<>();
        for (int i = 0; i < totalDays; i++) {
            CauHinhDiemDanh config = new CauHinhDiemDanh();
            config.setNgayThu(i + 1); // Ngày thứ 1, 2, 3...
            config.setSoXuThuong(initialValue + (i * step)); // Công thức cấp số cộng: u_n = u_1 + (n-1)*d
            config.setTrangThai(true);
            listToSave.add(config);
        }

        cauHinhDiemDanhRepository.saveAll(listToSave);
    }
}
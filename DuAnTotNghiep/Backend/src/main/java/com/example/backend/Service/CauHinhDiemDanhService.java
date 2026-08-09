package com.example.backend.Service;

import com.example.backend.Entity.CauHinhDiemDanh;
import java.util.List;

public interface CauHinhDiemDanhService {
    List<CauHinhDiemDanh> getAllCauHinh();
    CauHinhDiemDanh updateCauHinh(Integer id, CauHinhDiemDanh newInfo);
    CauHinhDiemDanh createCauHinh(CauHinhDiemDanh config);
    void deleteCauHinh(Integer id);
    void generateCauHinhCapSoCong(int totalDays, int initialValue, int step);
}
package com.example.backend.Service;

import com.example.backend.Entity.KichThuoc;
import com.example.backend.Repository.KichThuocRepository;
import com.example.backend.Request.KichThuocRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KichThuocService {

    @Autowired
    private KichThuocRepository kichThuocRepository;

    // ================= CREATE =================
    public KichThuoc create(KichThuocRequest req) {

        KichThuoc kt = new KichThuoc();

        kt.setMaKichThuoc(req.getMaKichThuoc());
        kt.setTenKichThuoc(req.getTenKichThuoc());
        kt.setTrangThai(req.getTrangThai() != null ? req.getTrangThai() : true);

        return kichThuocRepository.save(kt);
    }

    // ================= UPDATE =================
    public KichThuoc update(Integer id, KichThuocRequest req) {

        KichThuoc kt = kichThuocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước"));

        kt.setMaKichThuoc(req.getMaKichThuoc());
        kt.setTenKichThuoc(req.getTenKichThuoc());
        kt.setTrangThai(req.getTrangThai());

        return kichThuocRepository.save(kt);
    }

    // ================= DELETE =================
    public void delete(Integer id) {
        kichThuocRepository.deleteById(id);
    }

    // ================= GET ALL =================
    public List<KichThuoc> getAll() {
        return kichThuocRepository.findAll();
    }

    // ================= GET BY ID =================
    public KichThuoc getById(Integer id) {
        return kichThuocRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kích thước"));
    }
}
package com.example.backend.Service;

import com.example.backend.Entity.DanhMuc;
import com.example.backend.Repository.DanhMucRepository;
import com.example.backend.Request.DanhMucRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    // ================= CREATE =================
    public DanhMuc create(DanhMucRequest req) {

        DanhMuc dm = new DanhMuc();

        dm.setMaDanhMuc(req.getMaDanhMuc());
        dm.setTenDanhMuc(req.getTenDanhMuc());
        dm.setTrangThai(req.getTrangThai() != null ? req.getTrangThai() : true);

        return danhMucRepository.save(dm);
    }

    // ================= UPDATE =================
    public DanhMuc update(Integer id, DanhMucRequest req) {

        DanhMuc dm = danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        dm.setMaDanhMuc(req.getMaDanhMuc());
        dm.setTenDanhMuc(req.getTenDanhMuc());
        dm.setTrangThai(req.getTrangThai());

        return danhMucRepository.save(dm);
    }

    // ================= DELETE =================
    public void delete(Integer id) {
        danhMucRepository.deleteById(id);
    }

    // ================= GET ALL =================
    public List<DanhMuc> getAll() {
        return danhMucRepository.findAll();
    }

    // ================= GET BY ID =================
    public DanhMuc getById(Integer id) {
        return danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
    }
}
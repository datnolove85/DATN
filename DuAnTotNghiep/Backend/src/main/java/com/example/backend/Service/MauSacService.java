package com.example.backend.Service;

import com.example.backend.Entity.MauSac;
import com.example.backend.Repository.MauSacRepository;
import com.example.backend.Request.MauSacRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacService {

    @Autowired
    private MauSacRepository mauSacRepository;

    // ================= CREATE =================
    public MauSac create(MauSacRequest req) {

        MauSac ms = new MauSac();

        ms.setMaMauSac(req.getMaMauSac());
        ms.setTenMauSac(req.getTenMauSac());
        ms.setTrangThai(req.getTrangThai() != null ? req.getTrangThai() : true);

        return mauSacRepository.save(ms);
    }

    // ================= UPDATE =================
    public MauSac update(Integer id, MauSacRequest req) {

        MauSac ms = mauSacRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc"));

        ms.setMaMauSac(req.getMaMauSac());
        ms.setTenMauSac(req.getTenMauSac());
        ms.setTrangThai(req.getTrangThai());

        return mauSacRepository.save(ms);
    }

    // ================= DELETE =================
    public void delete(Integer id) {
        mauSacRepository.deleteById(id);
    }

    // ================= GET ALL =================
    public List<MauSac> getAll() {
        return mauSacRepository.findAll();
    }

    // ================= GET BY ID =================
    public MauSac getById(Integer id) {
        return mauSacRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy màu sắc"));
    }
}
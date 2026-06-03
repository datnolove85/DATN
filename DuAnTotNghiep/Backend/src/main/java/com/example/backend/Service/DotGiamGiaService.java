package com.example.backend.Service;

import com.example.backend.Entity.DotGiamGia;
import com.example.backend.Repository.DotGiamGiaRepository;
import com.example.backend.Request.DotGiamGiaRequest;
import com.example.backend.Response.DotGiamGiaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DotGiamGiaService {

    @Autowired
    private DotGiamGiaRepository repo;

    // ================== GET ALL ==================
    public List<DotGiamGiaResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    // ================== GET BY ID ==================
    public DotGiamGiaResponse getById(Integer id) {
        DotGiamGia e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá"));
        return map(e);
    }

    // ================== CREATE ==================
    public DotGiamGiaResponse create(DotGiamGiaRequest r) {

        if (repo.existsByMaDotGiamGia(r.getMaDotGiamGia())) {
            throw new RuntimeException("Mã đợt giảm giá đã tồn tại");
        }

        DotGiamGia e = new DotGiamGia();
        e.setMaDotGiamGia(r.getMaDotGiamGia());
        e.setTenDotGiamGia(r.getTenDotGiamGia());
        e.setLoaiGiamGia(r.getLoaiGiamGia());
        e.setGiaTriGiam(r.getGiaTriGiam());
        e.setGiaTriGiamToiDa(r.getGiaTriGiamToiDa());
        e.setNgayBatDau(r.getNgayBatDau());
        e.setNgayKetThuc(r.getNgayKetThuc());
        e.setMoTa(r.getMoTa());
        e.setTrangThai(r.getTrangThai() != null ? r.getTrangThai() : true);

        e.setNgayTao(Instant.now());
        e.setNgayCapNhat(Instant.now());

        return map(repo.save(e));
    }

    // ================== UPDATE ==================
    public DotGiamGiaResponse update(Integer id, DotGiamGiaRequest r) {

        DotGiamGia e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá"));

        e.setMaDotGiamGia(r.getMaDotGiamGia());
        e.setTenDotGiamGia(r.getTenDotGiamGia());
        e.setLoaiGiamGia(r.getLoaiGiamGia());
        e.setGiaTriGiam(r.getGiaTriGiam());
        e.setGiaTriGiamToiDa(r.getGiaTriGiamToiDa());
        e.setNgayBatDau(r.getNgayBatDau());
        e.setNgayKetThuc(r.getNgayKetThuc());
        e.setMoTa(r.getMoTa());
        e.setTrangThai(r.getTrangThai());

        e.setNgayCapNhat(Instant.now());

        return map(repo.save(e));
    }

    // ================== DELETE ==================
    public void delete(Integer id) {
        repo.deleteById(id);
    }

    // ================== MAPPER ==================
    private DotGiamGiaResponse map(DotGiamGia e) {
        DotGiamGiaResponse r = new DotGiamGiaResponse();
        r.setId(e.getId());
        r.setMaDotGiamGia(e.getMaDotGiamGia());
        r.setTenDotGiamGia(e.getTenDotGiamGia());
        r.setLoaiGiamGia(e.getLoaiGiamGia());
        r.setGiaTriGiam(e.getGiaTriGiam());
        r.setGiaTriGiamToiDa(e.getGiaTriGiamToiDa());
        r.setNgayBatDau(e.getNgayBatDau());
        r.setNgayKetThuc(e.getNgayKetThuc());
        r.setMoTa(e.getMoTa());
        r.setTrangThai(e.getTrangThai());
        return r;
    }
}
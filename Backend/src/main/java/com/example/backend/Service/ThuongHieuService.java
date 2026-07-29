package com.example.backend.Service;

import com.example.backend.Entity.ThuongHieu;
import com.example.backend.Repository.ThuongHieuRepository;
import com.example.backend.Request.ThuongHieuRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuongHieuService {

    @Autowired
    private ThuongHieuRepository repo;

    public List<ThuongHieu> getAll() {
        return repo.findAll();
    }

    public ThuongHieu getById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));
    }

    public ThuongHieu create(ThuongHieuRequest req) {
        ThuongHieu th = new ThuongHieu();
        th.setMaThuongHieu(req.getMaThuongHieu());
        th.setTenThuongHieu(req.getTenThuongHieu());
        th.setTrangThai(req.getTrangThai() != null ? req.getTrangThai() : true);
        return repo.save(th);
    }

    public ThuongHieu update(Integer id, ThuongHieuRequest req) {
        ThuongHieu th = getById(id);
        th.setMaThuongHieu(req.getMaThuongHieu());
        th.setTenThuongHieu(req.getTenThuongHieu());
        th.setTrangThai(req.getTrangThai());
        return repo.save(th);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
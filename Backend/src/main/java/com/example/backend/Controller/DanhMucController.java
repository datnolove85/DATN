package com.example.backend.Controller;

import com.example.backend.Entity.DanhMuc;
import com.example.backend.Repository.DanhMucRepository;
import com.example.backend.Request.DanhMucRequest;
import com.example.backend.Service.DanhMucService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/danhmuc")
@CrossOrigin("*")
public class DanhMucController {

    @Autowired
    private DanhMucService danhMucService;

    @Autowired
    DanhMucRepository danhMucRepository;

    // ================= GET ALL =================
    @GetMapping
    public List<DanhMuc> getAll() {
        return danhMucService.getAll();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public DanhMuc getById(@PathVariable Integer id) {
        return danhMucService.getById(id);
    }

    // ================= CREATE =================
    @PostMapping
    public DanhMuc create(@RequestBody DanhMucRequest req) {
        return danhMucService.create(req);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public DanhMuc update(@PathVariable Integer id,
                          @RequestBody DanhMucRequest req) {
        return danhMucService.update(id, req);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        DanhMuc danhMuc = danhMucService.getById(id);
        danhMuc.setTrangThai(false);
        danhMucRepository.save(danhMuc);
    }
}
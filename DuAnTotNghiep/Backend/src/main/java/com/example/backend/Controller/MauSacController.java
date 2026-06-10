package com.example.backend.Controller;

import com.example.backend.Entity.MauSac;
import com.example.backend.Repository.MauSacRepository;
import com.example.backend.Request.MauSacRequest;
import com.example.backend.Service.MauSacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mausac")
@CrossOrigin("*")
public class MauSacController {

    @Autowired
    private MauSacService mauSacService;

    @Autowired
    private MauSacRepository mauSacRepository;

    // ================= GET ALL =================
    @GetMapping
    public List<MauSac> getAll() {
        return mauSacService.getAll();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public MauSac getById(@PathVariable Integer id) {
        return mauSacService.getById(id);
    }

    // ================= CREATE =================
    @PostMapping
    public MauSac create(@RequestBody MauSacRequest req) {
        return mauSacService.create(req);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public MauSac update(@PathVariable Integer id,
                         @RequestBody MauSacRequest req) {
        return mauSacService.update(id, req);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
         MauSac mauSac = mauSacService.getById(id);
         mauSac.setTrangThai(false);
         mauSacRepository.save(mauSac);
    }
}
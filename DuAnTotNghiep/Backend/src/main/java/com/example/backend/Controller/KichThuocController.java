package com.example.backend.Controller;

import com.example.backend.Entity.KichThuoc;
import com.example.backend.Request.KichThuocRequest;
import com.example.backend.Service.KichThuocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kichthuoc")
@CrossOrigin("*")
public class KichThuocController {

    @Autowired
    private KichThuocService kichThuocService;

    // ================= GET ALL =================
    @GetMapping
    public List<KichThuoc> getAll() {
        return kichThuocService.getAll();
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public KichThuoc getById(@PathVariable Integer id) {
        return kichThuocService.getById(id);
    }

    // ================= CREATE =================
    @PostMapping
    public KichThuoc create(@RequestBody KichThuocRequest req) {
        return kichThuocService.create(req);
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public KichThuoc update(@PathVariable Integer id,
                            @RequestBody KichThuocRequest req) {
        return kichThuocService.update(id, req);
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        kichThuocService.delete(id);
    }
}
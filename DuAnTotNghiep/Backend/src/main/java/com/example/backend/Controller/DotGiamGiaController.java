package com.example.backend.Controller;

import com.example.backend.Request.DotGiamGiaRequest;
import com.example.backend.Response.DotGiamGiaResponse;
import com.example.backend.Service.DotGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/dot-giam-gia")
public class DotGiamGiaController {

    @Autowired
    private DotGiamGiaService service;

    // ================= GET ALL =================
    @GetMapping
    public List<DotGiamGiaResponse> getAll() {
        return service.getAll();
    }

    // ================= CREATE =================
    @PostMapping
    public DotGiamGiaResponse create(@RequestBody DotGiamGiaRequest req) {
        return service.create(req);
    }

    // ================= UPDATE =================
    @PutMapping("/update/{id}")
    public DotGiamGiaResponse update(@PathVariable Integer id,
                                     @RequestBody DotGiamGiaRequest req) {
        return service.update(id, req);
    }

    // ================= DELETE =================
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "Xóa đợt giảm giá thành công";
    }

    // ================= GET BY ID =================
    @GetMapping("/detail/{id}")
    public DotGiamGiaResponse getById(@PathVariable Integer id) {
        return service.getById(id);
    }
}
package com.example.backend.Controller;

import com.example.backend.Entity.ThuongHieu;
import com.example.backend.Request.ThuongHieuRequest;
import com.example.backend.Service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thuonghieu")
@CrossOrigin("*")
public class ThuongHieuController {

    @Autowired
    private ThuongHieuService service;

    @GetMapping
    public List<ThuongHieu> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ThuongHieu getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public ThuongHieu create(@RequestBody ThuongHieuRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public ThuongHieu update(@PathVariable Integer id,
                             @RequestBody ThuongHieuRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
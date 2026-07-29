package com.example.backend.Controller;

import com.example.backend.Entity.DiaChiKhachHang;
import com.example.backend.Request.DiaChiKhachHangRequest;
import com.example.backend.Service.DiaChiKhachHangService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dia-chi")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DiaChiKhachHangController {

    private final DiaChiKhachHangService service;

    @GetMapping
    public List<DiaChiKhachHang> getAll(HttpServletRequest request) {
        return service.getAll(request);
    }

    @GetMapping("/mac-dinh")
    public DiaChiKhachHang getMacDinh(HttpServletRequest request) {
        return service.getDiaChiMacDinh(request);
    }

    @PostMapping
    public DiaChiKhachHang add(@RequestBody DiaChiKhachHangRequest request,
                               HttpServletRequest servletRequest) {
        return service.add(request, servletRequest);
    }

    @PutMapping("/{id}")
    public DiaChiKhachHang update(@PathVariable Integer id,
                                  @RequestBody DiaChiKhachHangRequest request,
                                  HttpServletRequest servletRequest) {
        return service.update(id, request, servletRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id,
                       HttpServletRequest servletRequest) {
        service.delete(id, servletRequest);
    }

    @PutMapping("/mac-dinh/{id}")
    public void doiMacDinh(@PathVariable Integer id,
                           HttpServletRequest servletRequest) {
        service.doiMacDinh(id, servletRequest);
    }
}
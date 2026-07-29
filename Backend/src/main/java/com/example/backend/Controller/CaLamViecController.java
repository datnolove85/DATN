package com.example.backend.Controller;

import com.example.backend.Request.CaLamViecRequest;
import com.example.backend.Response.CaLamViecResponse;
import com.example.backend.Service.CaLamViecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shop-ban-quan-ao/ca-lam-viec")
@CrossOrigin(origins = "*")
public class CaLamViecController {
    @Autowired
    CaLamViecService caLamViecService;
    @GetMapping("/page")
    public List<CaLamViecResponse> phanTrang(@RequestParam(value = "pageNo",defaultValue = "0") Integer pageNo,
                                             @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize) {
         return caLamViecService.phanTrangCaLamViec(pageNo,pageSize).getContent();
    }
    @DeleteMapping("/delete/id")
    public void deleteCaLamViec(Integer id) {
        caLamViecService.deleleteCaLamViec(id);
    }
    @PostMapping("/add")
    public void addCaLamViec(@RequestBody CaLamViecRequest caLamViecRequest) {
        caLamViecService.addCaLamViec(caLamViecRequest);
    }
    @PutMapping("/update/id")
    public void updateCaLamViec(@RequestBody CaLamViecRequest caLamViecRequest,Integer id) {
        caLamViecService.updateCaLamViec(id,caLamViecRequest);
    }
}

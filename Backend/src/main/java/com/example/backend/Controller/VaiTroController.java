package com.example.backend.Controller;

import com.example.backend.Response.VaiTroResponse;
import com.example.backend.Service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/shop-ban-quan-ao/vai-tro")
@CrossOrigin(origins = "*")
public class VaiTroController {
    @Autowired
    VaiTroService vaiTroService;

    @GetMapping
    public List<VaiTroResponse> getVaiTro() {
        return vaiTroService.getAllVaiTro();
    }
}

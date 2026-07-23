package com.example.backend.Controller;

import com.example.backend.Request.ShippingFeeRequest;
import com.example.backend.Service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ghn")
@CrossOrigin("*")
public class GhnController {

    @Autowired
    private GHNService ghnService;

    @PostMapping("/fee")
    public Integer fee(@RequestBody ShippingFeeRequest request){

        return ghnService.tinhPhiVanChuyen(
                request.getThanhPho(),
                request.getQuan(),
                request.getPhuong()
        );
    }
}
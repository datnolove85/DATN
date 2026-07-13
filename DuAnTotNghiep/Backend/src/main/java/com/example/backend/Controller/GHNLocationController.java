package com.example.backend.Controller;

import com.example.backend.Service.GHNLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ghn")
@RequiredArgsConstructor
@CrossOrigin("*")
public class GHNLocationController {

    private final GHNLocationService ghnLocationService;

    @GetMapping("/provinces")
    public Object getProvinces() {

        return ghnLocationService.getProvinces();

    }

    @GetMapping("/districts/{provinceId}")
    public Object getDistricts(
            @PathVariable Integer provinceId
    ){

        return ghnLocationService.getDistricts(provinceId);

    }



    @GetMapping("/wards/{districtId}")
    public Object getWards(
            @PathVariable Integer districtId
    ){

        return ghnLocationService.getWards(districtId);

    }

}
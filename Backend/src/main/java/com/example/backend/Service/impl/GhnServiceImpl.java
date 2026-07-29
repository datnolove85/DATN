package com.example.backend.Service.impl;


import com.example.backend.Service.GHNMasterDataService;
import com.example.backend.Service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GhnServiceImpl implements GHNService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GHNMasterDataService masterDataService;

    @Value("${ghn.token}")
    private String token;

    @Value("${ghn.shop-id}")
    private Integer shopId;

    @Override
    public Integer tinhPhiVanChuyen(
            String thanhPho,
            String quan,
            String phuong
    ) {

        Integer provinceId =
                masterDataService.getProvinceId(thanhPho);

        System.out.println("Province = " + thanhPho);
        System.out.println("ProvinceId = " + provinceId);

        Integer districtId =
                masterDataService.getDistrictId(
                        provinceId,
                        quan
                );

        System.out.println("District = " + quan);
        System.out.println("DistrictId = " + districtId);

        String wardCode =
                masterDataService.getWardCode(
                        districtId,
                        phuong
                );

        System.out.println("Ward = " + phuong);
        System.out.println("WardCode = " + wardCode);
        String url =
                "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";

        HttpHeaders headers = new HttpHeaders();

        headers.set("Token", token);
        headers.set("ShopId", shopId.toString());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();

        body.put("service_type_id", 2);

        // Quận cửa hàng
        body.put("from_district_id", 1442);

        // Quận khách
        body.put("to_district_id", districtId);

        // Phường khách
        body.put("to_ward_code", wardCode);

        body.put("height", 10);
        body.put("length", 20);
        body.put("width", 20);
        body.put("weight", 500);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);
        System.out.println("===== GHN REQUEST =====");
        System.out.println(body);
        System.out.println("=======================");
        ResponseEntity<Map> response =

                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );

        Map data =
                (Map) response.getBody().get("data");

        return (Integer) data.get("total");
    }
}
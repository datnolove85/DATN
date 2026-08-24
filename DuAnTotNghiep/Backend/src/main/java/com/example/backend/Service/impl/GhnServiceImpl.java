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
    @Override
    public Long tinhThoiGianGiaoHang(
            String thanhPho,
            String quan,
            String phuong
    ) {
        // 1. Lấy ID địa chính giống như lúc tính phí
        Integer provinceId = masterDataService.getProvinceId(thanhPho);
        Integer districtId = masterDataService.getDistrictId(provinceId, quan);
        String wardCode = masterDataService.getWardCode(districtId, phuong);

        // 2. URL API Leadtime của GHN
        String url = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime";

        // 3. Thiết lập Header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.set("ShopId", shopId.toString());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 4. Thiết lập Body (API leadtime yêu cầu from_district, to_district, to_ward và service_id)
        Map<String, Object> body = new HashMap<>();
        body.put("from_district_id", 1442); // Quận của cửa hàng (giống lúc tính phí)
        body.put("to_district_id", districtId);
        body.put("to_ward_code", wardCode);
        body.put("service_id", 53320); // ID dịch vụ chuẩn của GHN (ví dụ: Dịch vụ Gói nhanh/Chuẩn)

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getBody() != null && response.getBody().get("data") != null) {
                Map data = (Map) response.getBody().get("data");

                // GHN trả về leadtime dạng Unix Timestamp (kiểu Long hoặc Number)
                Number leadtimeNumber = (Number) data.get("leadtime");
                if (leadtimeNumber != null) {
                    return leadtimeNumber.longValue();
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi gọi API Leadtime GHN: " + e.getMessage());
        }

        return null;
    }
}
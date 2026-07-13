package com.example.backend.Service.impl;

import com.example.backend.Service.GHNMasterDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GHNMasterDataServiceImpl implements GHNMasterDataService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ghn.token}")
    private String token;

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    //=================== Province ===================

    @Override
    public Integer getProvinceId(String provinceName) {

        String url =
                "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";

        HttpEntity<?> entity = new HttpEntity<>(headers());

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        Map.class
                );

        List<Map<String,Object>> provinces =
                (List<Map<String,Object>>) response.getBody().get("data");

        for(Map<String,Object> p : provinces){

            String name = p.get("ProvinceName").toString();

            String shortName = name
                    .replace("Thành phố ", "")
                    .replace("Tỉnh ", "")
                    .trim();

            if(name.equalsIgnoreCase(provinceName)
                    || shortName.equalsIgnoreCase(provinceName)
                    || name.contains(provinceName)
                    || provinceName.contains(shortName)){

                return (Integer)p.get("ProvinceID");
            }
        }

        throw new RuntimeException("Không tìm thấy tỉnh: " + provinceName);
    }

    //=================== District ===================

    @Override
    public Integer getDistrictId(Integer provinceId, String districtName) {

        String url =
                "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";

        HttpHeaders headers = headers();

        Map<String,Object> body =
                Map.of("province_id", provinceId);

        HttpEntity<Map<String,Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );

        List<Map<String,Object>> districts =
                (List<Map<String,Object>>) response.getBody().get("data");

        for(Map<String,Object> d : districts){

            String name = d.get("DistrictName").toString();

            String shortName = name
                    .replace("Quận ", "")
                    .replace("Huyện ", "")
                    .replace("Thành phố ", "")
                    .replace("Thị xã ", "")
                    .trim();

            if(name.equalsIgnoreCase(districtName)
                    || shortName.equalsIgnoreCase(districtName)
                    || name.contains(districtName)
                    || districtName.contains(shortName)){

                return (Integer)d.get("DistrictID");
            }
        }

        throw new RuntimeException("Không tìm thấy quận: " + districtName);
    }

    //=================== Ward ===================

    @Override
    public String getWardCode(Integer districtId, String wardName) {

        String url =
                "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";

        HttpHeaders headers = headers();

        Map<String,Object> body =
                Map.of("district_id", districtId);

        HttpEntity<Map<String,Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );

        List<Map<String,Object>> wards =
                (List<Map<String,Object>>) response.getBody().get("data");

        for(Map<String,Object> w : wards){

            String name = w.get("WardName").toString();

            String shortName = name
                    .replace("Phường ", "")
                    .replace("Xã ", "")
                    .replace("Thị trấn ", "")
                    .trim();

            if(name.equalsIgnoreCase(wardName)
                    || shortName.equalsIgnoreCase(wardName)
                    || name.contains(wardName)
                    || wardName.contains(shortName)){

                return w.get("WardCode").toString();
            }
        }

        throw new RuntimeException("Không tìm thấy phường: " + wardName);
    }
}
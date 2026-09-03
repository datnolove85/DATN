package com.example.backend.Service.impl;

import com.example.backend.Service.GHNLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GHNLocationServiceImpl implements GHNLocationService {

    private final RestTemplate restTemplate;

    @Value("${ghn.token}")
    private String token;

    @Override
    public List<Map<String, Object>> getProvinces() {

        String url =
                "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Token", token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        Map.class
                );

        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    @Override
    public List<Map<String, Object>> getDistricts(Integer provinceId) {


        String url =
                "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";


        HttpHeaders headers = new HttpHeaders();

        headers.set("Token", token);


        Map<String, Object> body = new HashMap<>();

        body.put(
                "province_id",
                provinceId
        );


        HttpEntity<?> entity =
                new HttpEntity<>(
                        body,
                        headers
                );


        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );


        return (List<Map<String, Object>>)
                response.getBody()
                        .get("data");

    }

    @Override
    public List<Map<String, Object>> getWards(Integer districtId) {


        String url =
                "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";


        HttpHeaders headers =
                new HttpHeaders();


        headers.set(
                "Token",
                token
        );


        Map<String, Object> body =
                new HashMap<>();


        body.put(
                "district_id",
                districtId
        );


        HttpEntity<?> entity =
                new HttpEntity<>(
                        body,
                        headers
                );


        ResponseEntity<Map> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Map.class
                );


        return (List<Map<String, Object>>)
                response.getBody()
                        .get("data");

    }
}
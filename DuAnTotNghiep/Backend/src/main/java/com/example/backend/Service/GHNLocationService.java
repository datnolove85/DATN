package com.example.backend.Service;

import java.util.List;
import java.util.Map;

public interface GHNLocationService {

    List<Map<String, Object>> getProvinces();

    List<Map<String, Object>> getDistricts(Integer provinceId);

    List<Map<String, Object>> getWards(Integer districtId);

}
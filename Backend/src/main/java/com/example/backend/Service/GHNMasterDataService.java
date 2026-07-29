package com.example.backend.Service;

public interface GHNMasterDataService {

    Integer getProvinceId(String provinceName);

    Integer getDistrictId(Integer provinceId, String districtName);

    String getWardCode(Integer districtId, String wardName);

}
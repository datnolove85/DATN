package com.example.backend.mapper;


import com.example.backend.Response.DotGiamGiaDetailResponse;
import com.example.backend.Response.DotGiamGiaResponse;
import com.example.backend.Entity.DotGiamGia;

import java.time.Instant;

public class DotGiamGiaMapper {

    private DotGiamGiaMapper() {
    }

    /**
     * Entity -> Response danh sách
     */
    public static DotGiamGiaResponse toResponse(
            DotGiamGia entity,
            Integer tongSanPham
    ) {

        DotGiamGiaResponse response = new DotGiamGiaResponse();

        response.setId(entity.getId());
        response.setMaDotGiamGia(entity.getMaDotGiamGia());
        response.setTenDotGiamGia(entity.getTenDotGiamGia());
        response.setLoaiGiamGia(entity.getLoaiGiamGia());
        response.setGiaTriGiam(entity.getGiaTriGiam());
        response.setGiaTriGiamToiDa(entity.getGiaTriGiamToiDa());
        response.setNgayBatDau(entity.getNgayBatDau());
        response.setNgayKetThuc(entity.getNgayKetThuc());

        response.setTrangThai(entity.getTrangThai());

        response.setTongSanPham(tongSanPham);

        return response;
    }

    /**
     * Entity -> Detail
     */
    public static DotGiamGiaDetailResponse toDetailResponse(
            DotGiamGia entity
    ) {

        DotGiamGiaDetailResponse response =
                new DotGiamGiaDetailResponse();

        response.setId(entity.getId());

        response.setMaDotGiamGia(entity.getMaDotGiamGia());

        response.setTenDotGiamGia(entity.getTenDotGiamGia());

        response.setLoaiGiamGia(entity.getLoaiGiamGia());

        response.setGiaTriGiam(entity.getGiaTriGiam());

        response.setGiaTriGiamToiDa(entity.getGiaTriGiamToiDa());

        response.setNgayBatDau(entity.getNgayBatDau());

        response.setNgayKetThuc(entity.getNgayKetThuc());

        response.setMoTa(entity.getMoTa());

        response.setTrangThai(entity.getTrangThai());

        return response;
    }

}
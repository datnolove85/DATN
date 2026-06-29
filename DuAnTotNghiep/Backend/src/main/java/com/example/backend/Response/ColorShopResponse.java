package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorShopResponse {

    private Integer id;

    private String name;

    private List<SanPhamChiTietResponse> variants;

}
package com.example.backend.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {

    private Integer productId;

    private String productName;

    private List<ColorShopResponse> colors;
}
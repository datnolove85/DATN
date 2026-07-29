package com.example.backend.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SizeShopResponse {

    private Integer variantId;

    private Integer id;

    private String name;

    private Integer stock;

    private BigDecimal price;
}
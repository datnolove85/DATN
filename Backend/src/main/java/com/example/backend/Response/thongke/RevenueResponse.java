package com.example.backend.Response.thongke;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevenueResponse {

    private String label;

    private BigDecimal revenue;

}
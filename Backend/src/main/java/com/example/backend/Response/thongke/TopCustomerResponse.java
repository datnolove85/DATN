package com.example.backend.Response.thongke;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopCustomerResponse {

    private Integer customerId;

    private String customerName;

    private Long totalOrders;

    private BigDecimal totalSpent;

}
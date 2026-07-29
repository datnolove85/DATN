package com.example.backend.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    private Integer idHoaDon;

    private String method;

    private BigDecimal amount;

    private String orderInfo;

}
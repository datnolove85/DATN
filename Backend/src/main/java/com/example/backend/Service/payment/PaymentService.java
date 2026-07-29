package com.example.backend.Service.payment;


import com.example.backend.Request.PaymentRequest;
import com.example.backend.Response.PaymentResponse;

public interface PaymentService {

    String getCode();

    PaymentResponse pay(PaymentRequest request);

}

package com.example.backend.Service.payment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentFactory {

    private final Map<String, PaymentService> services;

    public PaymentFactory(List<PaymentService> paymentServices) {

        services = paymentServices.stream()
                .collect(Collectors.toMap(
                        PaymentService::getCode,
                        Function.identity()
                ));
    }

    public PaymentService get(String code) {

        PaymentService service = services.get(code);

        if (service == null) {
            throw new RuntimeException("Không hỗ trợ phương thức " + code);
        }

        return service;
    }
}
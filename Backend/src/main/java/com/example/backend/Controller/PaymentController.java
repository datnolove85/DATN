package com.example.backend.Controller;

import com.example.backend.Request.PaymentRequest;
import com.example.backend.Response.PaymentResponse;

import com.example.backend.Service.payment.PaymentFactory;
import com.example.backend.Service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentFactory paymentFactory;

    @PostMapping("/pay")
    public PaymentResponse pay(@RequestBody PaymentRequest request) {

        return paymentFactory
                .get(request.getMethod())
                .pay(request);

    }

}
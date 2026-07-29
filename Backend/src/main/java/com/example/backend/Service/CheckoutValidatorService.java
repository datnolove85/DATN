package com.example.backend.Service;

import com.example.backend.Response.SyncOrderResult;

public interface CheckoutValidatorService {

    SyncOrderResult syncOrderBeforeCheckout(
            Integer idHoaDon
    );

}
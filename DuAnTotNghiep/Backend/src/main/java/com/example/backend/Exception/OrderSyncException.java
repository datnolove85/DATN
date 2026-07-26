package com.example.backend.Exception;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderSyncException extends RuntimeException {

    private final List<String> messages;

    public OrderSyncException(List<String> messages) {
        super("ORDER_UPDATED");
        this.messages = messages;
    }

}
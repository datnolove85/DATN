package com.example.backend.websocket;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherEvent {

    private String type;

    private Integer voucherId;

    private boolean needRevalidate;

    private List<String> changedFields;

}
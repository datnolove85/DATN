package com.example.backend.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PosEvent {

    private String type;

    private Integer orderId;

    private Integer productId;

    private Integer newStock;

}
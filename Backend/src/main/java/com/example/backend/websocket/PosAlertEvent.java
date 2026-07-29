package com.example.backend.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PosAlertEvent {

    private String type;

    private String message; // Câu thông báo chi tiết gửi sang POS

    private Integer productId;

    private Integer newStock;
}
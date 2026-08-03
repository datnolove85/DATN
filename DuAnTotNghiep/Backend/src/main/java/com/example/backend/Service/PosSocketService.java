package com.example.backend.Service;

import com.example.backend.websocket.PosAlertEvent; // 🔴 Import Class mới
import com.example.backend.websocket.PosEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PosSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Giữ nguyên hàm send cũ cho PosEvent
    public void send(PosEvent event) {
        messagingTemplate.convertAndSend("/topic/pos", event);
    }

    // 🟢 THÊM HÀM MỚI NÀY ĐỂ BẮN PosAlertEvent SANG /topic/pos
    public void send(PosAlertEvent event) {
        messagingTemplate.convertAndSend("/topic/pos", event);
    }

    public void notifyDiscountUpdated() {
        messagingTemplate.convertAndSend(
                "/topic/pos",
                new PosEvent("DISCOUNT_UPDATED", null, null, null)
        );
    }

    public void notifyKhoVoucherUpdated(Integer khoVoucherId) {
        messagingTemplate.convertAndSend(
                "/topic/pos",
                new PosEvent(
                        "KHO_VOUCHER_UPDATED",
                        null,
                        khoVoucherId,
                        null
                )
        );
    }
}
package com.example.backend.Service;

import com.example.backend.websocket.PosEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class PosSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void send(PosEvent event) {
        messagingTemplate.convertAndSend("/topic/pos", event);
    }
    public void notifyDiscountUpdated() {
        messagingTemplate.convertAndSend(
                "/topic/pos",
                new PosEvent("DISCOUNT_UPDATED", null, null, null)
        );
    }
}
package com.example.backend.Service.impl;

import com.example.backend.Service.CustomerSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerSocketServiceImpl implements CustomerSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void voucherUpdated(Integer voucherId) {

        messagingTemplate.convertAndSend(
                "/topic/customer/voucher",
                voucherId
        );

    }

}
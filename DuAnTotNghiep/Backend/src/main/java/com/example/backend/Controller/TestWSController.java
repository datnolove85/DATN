package com.example.backend.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestWSController {

    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public String test() {

        messagingTemplate.convertAndSend(
                "/topic/test",
                "HELLO POS"
        );

        return "OK";
    }
}
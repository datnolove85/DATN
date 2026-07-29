package com.example.backend.Controller;

import com.example.backend.Request.ChatbotMessageRequest;
import com.example.backend.Response.ChatbotMessageResponse;
import com.example.backend.Service.ChatbotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/messages")
    public ChatbotMessageResponse sendMessage(@Valid @RequestBody ChatbotMessageRequest request) {
        return chatbotService.answer(request);
    }
}

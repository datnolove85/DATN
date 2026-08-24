package com.example.backend.Controller;

import com.example.backend.Request.SendChatMessageRequest;
import com.example.backend.Response.ChatConversationResponse;
import com.example.backend.Response.ChatInboxResponse;
import com.example.backend.Response.ChatMessageResponse;
import com.example.backend.Service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/conversation")
    public ChatConversationResponse createOrGet(Authentication authentication) {
        return chatService.createOrGetConversation(requireAuth(authentication));
    }

    @GetMapping("/me")
    public ChatConversationResponse myConversation(Authentication authentication) {
        return chatService.getMyConversation(requireAuth(authentication));
    }

    @GetMapping("/inbox")
    public List<ChatInboxResponse> inbox(Authentication authentication) {
        return chatService.getInbox(requireAuth(authentication));
    }

    @GetMapping("/conversation/{id}")
    public ChatConversationResponse conversation(@PathVariable Integer id, Authentication authentication) {
        return chatService.getConversationForStaff(id, requireAuth(authentication));
    }

    @PostMapping("/conversation/{id}/message")
    public ChatMessageResponse send(@PathVariable Integer id, @Valid @RequestBody SendChatMessageRequest request, Authentication authentication) {
        return chatService.sendFromCustomer(id, requireAuth(authentication), request.getNoiDung(), request.getLoai());
    }

    @PostMapping("/conversation/{id}/reply")
    public ChatMessageResponse reply(@PathVariable Integer id, @Valid @RequestBody SendChatMessageRequest request, Authentication authentication) {
        return chatService.sendFromStaff(id, requireAuth(authentication), request.getNoiDung(), request.getLoai());
    }

    @PostMapping("/conversation/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable Integer id, Authentication authentication) {
        chatService.markRead(id, requireAuth(authentication));
        return ResponseEntity.ok(Map.of("message", "Đã đánh dấu đã đọc"));
    }

    @PostMapping("/conversation/{id}/close")
    public ResponseEntity<?> close(@PathVariable Integer id, Authentication authentication) {
        chatService.closeConversation(id, requireAuth(authentication));
        return ResponseEntity.ok(Map.of("message", "Đã đóng cuộc trò chuyện"));
    }

    private String requireAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) throw new RuntimeException("Bạn cần đăng nhập");
        return authentication.getName();
    }
}

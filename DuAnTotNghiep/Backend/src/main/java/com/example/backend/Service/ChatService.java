package com.example.backend.Service;

import com.example.backend.Response.ChatConversationResponse;
import com.example.backend.Response.ChatInboxResponse;
import com.example.backend.Response.ChatMessageResponse;

import java.util.List;

public interface ChatService {
    ChatConversationResponse getMyConversation(String email);
    ChatConversationResponse getConversationForStaff(Integer idConversation, String email);
    List<ChatInboxResponse> getInbox(String email);
    ChatMessageResponse sendFromCustomer(Integer idConversation, String email, String content, String type);
    ChatMessageResponse sendFromStaff(Integer idConversation, String email, String content, String type);
    ChatConversationResponse createOrGetConversation(String email);
    void markRead(Integer idConversation, String email);
    void closeConversation(Integer idConversation, String email);
}

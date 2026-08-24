package com.example.backend.Repository;

import com.example.backend.Entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversation_IdOrderByThoiGianAsc(Integer idConversation);
    long countByConversation_IdAndDaDocFalseAndNguoiGui_IdNot(Integer idConversation, Integer idNguoiGui);
}

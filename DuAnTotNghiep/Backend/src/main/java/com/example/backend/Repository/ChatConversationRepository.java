package com.example.backend.Repository;

import com.example.backend.Entity.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatConversationRepository extends JpaRepository<ChatConversation, Integer> {
    Optional<ChatConversation> findByKhachHang_Id(Integer idKhachHang);
    List<ChatConversation> findAllByOrderByTinNhanCuoiLucDesc();
}

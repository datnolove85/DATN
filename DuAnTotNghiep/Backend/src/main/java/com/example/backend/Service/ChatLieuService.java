package com.example.backend.Service;

import com.example.backend.Entity.ChatLieu;
import com.example.backend.Repository.ChatLieuRepository;
import com.example.backend.Request.ChatLieuRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatLieuService {

    @Autowired
    private ChatLieuRepository repo;

    public List<ChatLieu> getAll() {
        return repo.findAll();
    }

    public ChatLieu getById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu"));
    }

    public ChatLieu create(ChatLieuRequest req) {
        ChatLieu cl = new ChatLieu();
        cl.setMaChatLieu(req.getMaChatLieu());
        cl.setTenChatLieu(req.getTenChatLieu());
        cl.setTrangThai(req.getTrangThai() != null ? req.getTrangThai() : true);
        return repo.save(cl);
    }

    public ChatLieu update(Integer id, ChatLieuRequest req) {
        ChatLieu cl = getById(id);
        cl.setMaChatLieu(req.getMaChatLieu());
        cl.setTenChatLieu(req.getTenChatLieu());
        cl.setTrangThai(req.getTrangThai());
        return repo.save(cl);
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
package com.example.backend.Controller;

import com.example.backend.Entity.ChatLieu;
import com.example.backend.Repository.ChatLieuRepository;
import com.example.backend.Request.ChatLieuRequest;
import com.example.backend.Service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatlieu")
@CrossOrigin("*")
public class ChatLieuController {

    @Autowired
    private ChatLieuService service;

    @Autowired
    private ChatLieuRepository lieuRepository;

    @GetMapping
    public List<ChatLieu> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ChatLieu getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public ChatLieu create(@RequestBody ChatLieuRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public ChatLieu update(@PathVariable Integer id,
                           @RequestBody ChatLieuRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        ChatLieu lieu = lieuRepository.getById(id);
        lieu.setTrangThai(false);
        lieuRepository.save(lieu);
    }
}
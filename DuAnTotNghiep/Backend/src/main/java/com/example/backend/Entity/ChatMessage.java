package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_conversation", nullable = false)
    private ChatConversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_nguoi_gui", nullable = false)
    private TaiKhoan nguoiGui;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nguoi_nhan")
    private TaiKhoan nguoiNhan;

    @Column(name = "noi_dung", nullable = false, length = 4000)
    private String noiDung;

    @Column(name = "loai", nullable = false, length = 20)
    private String loai = "TEXT";

    @Column(name = "da_doc", nullable = false)
    private boolean daDoc = false;

    @Column(name = "thoi_gian", nullable = false)
    private Instant thoiGian = Instant.now();
}

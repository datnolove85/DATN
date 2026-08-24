package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ChatMessageResponse {
    private Long id;
    private Integer idConversation;
    private Integer idNguoiGui;
    private String tenNguoiGui;
    private Integer idNguoiNhan;
    private String tenNguoiNhan;
    private String noiDung;
    private String loai;
    private boolean daDoc;
    private Instant thoiGian;
    private boolean cuaToi;
}

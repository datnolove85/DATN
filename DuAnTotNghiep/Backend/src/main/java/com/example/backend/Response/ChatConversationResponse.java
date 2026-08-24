package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatConversationResponse {
    private Integer id;
    private Integer idKhachHang;
    private String tenKhachHang;
    private String anhKhachHang;
    private Integer idNhanVien;
    private String tenNhanVien;
    private String trangThai;
    private Instant tinNhanCuoiLuc;
    private long soTinChuaDoc;
    private List<ChatMessageResponse> messages;
}

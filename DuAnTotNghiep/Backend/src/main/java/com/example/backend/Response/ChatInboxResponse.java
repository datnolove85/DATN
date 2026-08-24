package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ChatInboxResponse {
    private Integer id;
    private Integer idKhachHang;
    private String tenKhachHang;
    private String anhKhachHang;
    private Integer idNhanVien;
    private String tenNhanVien;
    private String trangThai;
    private String tinNhanCuoi;
    private Instant tinNhanCuoiLuc;
    private long soTinChuaDoc;
}

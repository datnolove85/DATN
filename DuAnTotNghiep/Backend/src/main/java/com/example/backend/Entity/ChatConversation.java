package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "chat_conversation")
public class ChatConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_khach_hang", nullable = false, unique = true)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien")
    private TaiKhoan nhanVien;

    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai = "OPEN";

    @Column(name = "ngay_tao", nullable = false)
    private Instant ngayTao = Instant.now();

    @Column(name = "ngay_cap_nhat", nullable = false)
    private Instant ngayCapNhat = Instant.now();

    @Column(name = "tin_nhan_cuoi_luc")
    private Instant tinNhanCuoiLuc;
}

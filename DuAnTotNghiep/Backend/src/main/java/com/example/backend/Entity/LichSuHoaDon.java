package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lich_su_hoa_don")
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class LichSuHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @Column(name = "trang_thai_cu")
    private String trangThaiCu;

    @Column(name = "trang_thai_moi")
    private String trangThaiMoi;

    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien")
    private NhanVien nhanVien; // Có thể null nếu do khách hàng hoặc hệ thống tự động

    @Column(name = "ghi_chu")
    private String ghiChu;

    // Bạn có thể thêm trường nguồn_thao_tac nếu cần
    @Column(name = "nguon_thao_tac")
    private String nguonThaoTac; // 'STAFF', 'CUSTOMER', 'SYSTEM'

}
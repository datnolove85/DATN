package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cau_hinh_diem_danh")
public class CauHinhDiemDanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "ngay_thu", nullable = false, unique = true)
    private Integer ngayThu; // Ngày thứ mấy trong chuỗi (1, 2, 3...)

    @NotNull
    @Column(name = "so_xu_thuong", nullable = false)
    private Integer soXuThuong; // Số xu thưởng tương ứng

    @Column(name = "trang_thai")
    private Boolean trangThai = true; // Trạng thái hoạt động (true/false)
}
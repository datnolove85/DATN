package com.example.backend.Response;

import com.example.backend.Entity.ChatLieu;
import com.example.backend.Entity.DanhMuc;
import com.example.backend.Entity.ThuongHieu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamResponse {

    private Integer id;

    private DanhMuc idDanhMuc;

    private ThuongHieu idThuongHieu;

    private ChatLieu idChatLieu;

    private String maSanPham;

    private String tenSanPham;

    private String moTa;

    private Date ngayTao;

    private Date ngayCapNhat;

    private Boolean trangThai;

    private String image;
}
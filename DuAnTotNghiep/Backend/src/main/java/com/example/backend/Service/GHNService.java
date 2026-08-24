package com.example.backend.Service;

public interface GHNService {

        Integer tinhPhiVanChuyen(
                String thanhPho,
                String quan,
                String phuong
        );
        Long tinhThoiGianGiaoHang(
                String thanhPho,
                String quan,
                String phuong
        );

}
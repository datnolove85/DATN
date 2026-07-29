package com.example.backend.Response;

import lombok.Data;

@Data
public class TheoDoiDonHangDTO {

    private boolean choXacNhan;

    private boolean daXacNhan;

    private boolean choVanChuyen; // 🌟 Bổ sung

    private boolean dangGiao;

    private boolean giaoThanhCong; // 🌟 Đổi từ daGiao -> giaoThanhCong cho đồng bộ

    private boolean giaoThatBai; // 🌟 Bổ sung

    private boolean hoanThanh;

    private boolean daHuy;
}
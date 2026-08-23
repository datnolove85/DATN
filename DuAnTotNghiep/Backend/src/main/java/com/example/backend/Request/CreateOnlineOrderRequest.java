package com.example.backend.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class CreateOnlineOrderRequest {

    private Integer idKhachHang;
    private Integer addressId;

    private String tenNguoiNhan;
    private String soDienThoaiNguoiNhan;
    private String thanhPho;
    private String quan;
    private String phuong;
    private String diaChiCuThe;
    private Integer districtId;
    private String wardCode;

    private BigDecimal shippingFee;
    private Integer voucherId;
    private Integer voucherKhachHangId;
    private Integer soXuSuDung;
    private String note;

    // 📌 BỔ SUNG TRƯỜNG NÀY ĐỂ NHẬN DỮ LIỆU TỪ FRONTEND
    private String paymentMethod;

    private List<Item> items;

    @Data
    public static class Item {
        private Integer productDetailId;
        private Integer quantity;
        private BigDecimal price;
    }
}
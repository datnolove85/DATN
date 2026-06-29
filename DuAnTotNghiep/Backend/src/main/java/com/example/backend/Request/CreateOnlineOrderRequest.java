package com.example.backend.Request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOnlineOrderRequest {

    private Integer idKhachHang;

    private Integer addressId;

    private BigDecimal shippingFee;

    private Integer voucherId;

    private String note;

    private List<Item> items;

    @Data
    public static class Item {
        private Integer productDetailId;
        private Integer quantity;
    }

}
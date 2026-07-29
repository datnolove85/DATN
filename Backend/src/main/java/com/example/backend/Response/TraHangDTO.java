package com.example.backend.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TraHangDTO {

    private boolean coTraHang;

    private String maTraHang;

    private String trangThai;

    private BigDecimal tongTienHoan;

    private String lyDo;

}
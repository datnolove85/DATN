package com.example.backend.Request;

import lombok.Data;

import java.util.List;

@Data
public class TraHangRequest {

    private Integer hoaDonId;

    private String lyDo;

    private List<SanPhamTraRequest> danhSachTra;

}
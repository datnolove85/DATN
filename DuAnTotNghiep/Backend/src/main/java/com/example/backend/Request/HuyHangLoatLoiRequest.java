package com.example.backend.Request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class HuyHangLoatLoiRequest {
    private List<Integer> danhSachHoaDonId;
    private String lyDoLoi;
}
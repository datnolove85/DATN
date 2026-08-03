package com.example.backend.Response;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LichSuXuResponse {
    private Integer id;
    private Integer soXuThayDoi;
    private Integer soDuTruoc;
    private Integer soDuSau;
    private String loaiGiaoDich;
    private String moTa;
    private LocalDateTime ngayTao;
}
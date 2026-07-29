package com.example.backend.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KichThuocResponse {
    private Integer id;
    private String maKichThuoc;
    private String tenKichThuoc;
    private Boolean trangThai;
}
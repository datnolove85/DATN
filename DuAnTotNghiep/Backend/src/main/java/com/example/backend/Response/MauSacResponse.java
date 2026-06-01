package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MauSacResponse {
    private Integer id;
    private String maMauSac;
    private String tenMauSac;
    private Boolean trangThai;
}
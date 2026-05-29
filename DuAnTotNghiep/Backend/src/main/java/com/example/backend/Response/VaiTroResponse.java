package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VaiTroResponse {
    private Integer id;
    private String maVaiTro;
    private String tenVaiTro;
    private Integer trangThai;
}

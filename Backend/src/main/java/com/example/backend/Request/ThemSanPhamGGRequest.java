package com.example.backend.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
@Getter
@Setter

public class ThemSanPhamGGRequest {

    private List<Integer> idsSanPhamChiTiet;
}

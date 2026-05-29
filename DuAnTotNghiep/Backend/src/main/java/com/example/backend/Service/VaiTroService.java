package com.example.backend.Service;

import com.example.backend.Response.VaiTroResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VaiTroService {
     Page<VaiTroResponse> phanTrangVaiTro(Integer pageNo, Integer pageSize);
     List<VaiTroResponse> getAllVaiTro();
}

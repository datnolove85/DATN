package com.example.backend.Service;

import com.example.backend.Request.CaLamViecRequest;
import com.example.backend.Response.CaLamViecResponse;
import org.springframework.data.domain.Page;

public interface CaLamViecService {
       Page<CaLamViecResponse> phanTrangCaLamViec(Integer pageNo, Integer pageSize);

       void deleleteCaLamViec(Integer id);
       void addCaLamViec(CaLamViecRequest caLamViecRequest);
       void updateCaLamViec(Integer id,CaLamViecRequest caLamViecRequest);
       CaLamViecResponse detailCaLamViec(Integer id);
}

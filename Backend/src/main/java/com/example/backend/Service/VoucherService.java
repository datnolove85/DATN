package com.example.backend.Service;

import com.example.backend.Entity.Voucher;
import com.example.backend.Request.VoucherRequest;
import com.example.backend.Response.VoucherResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VoucherService {
    Page<VoucherResponse> phanTrangVoucher(Integer pageNo, Integer pageSize);
    List<VoucherResponse> searchVoucher(String keyword);
    VoucherResponse detailVoucher(Integer id);
    VoucherResponse addVoucher(VoucherRequest voucherRequest);
    VoucherResponse updateVoucher(Integer id, VoucherRequest voucherRequest);
    void deleteVoucher(Integer id);
    List<Voucher> getAll();
}
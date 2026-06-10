package com.example.backend.Controller;

import com.example.backend.Entity.Voucher;
import com.example.backend.Request.VoucherRequest;
import com.example.backend.Response.VoucherResponse;
import com.example.backend.Service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
@CrossOrigin(origins = "*")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public List<Voucher> getAllVouchers() {
        return voucherService.getAll();
    }

    @GetMapping("/page")
    public List<VoucherResponse> phanTrang(@RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return voucherService.phanTrangVoucher(pageNo, pageSize).getContent();
    }

    @GetMapping("/search")
    public List<VoucherResponse> search(@RequestParam(defaultValue = "") String keyword) {
        return voucherService.searchVoucher(keyword);
    }

    @GetMapping("/detail/{id}")
    public VoucherResponse detail(@PathVariable Integer id) {
        return voucherService.detailVoucher(id);
    }

    @PostMapping("/add")
    public VoucherResponse add(@RequestBody VoucherRequest voucherRequest) {
        return voucherService.addVoucher(voucherRequest);
    }

    @PutMapping("/update/{id}")
    public VoucherResponse update(@PathVariable Integer id, @RequestBody VoucherRequest voucherRequest) {
        return voucherService.updateVoucher(id, voucherRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Integer id) {
        voucherService.deleteVoucher(id);
    }
}
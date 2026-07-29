package com.example.backend.Controller;

import com.example.backend.Response.thongke.*;
import com.example.backend.Service.ThongKeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/thong-ke")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ThongKeController {

    private final ThongKeService thongKeService;

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(required = false) String loaiHoaDon
    ) {
        return thongKeService.dashboard(from, to, loaiHoaDon);
    }

    @GetMapping("/doanh-thu")
    public List<RevenueResponse> revenue(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "day") String groupBy,
            @RequestParam(required = false) String loaiHoaDon
    ) {
        return thongKeService.revenue(from, to, groupBy, loaiHoaDon);
    }

    @GetMapping("/trang-thai-don")
    public List<OrderStatusResponse> thongKeTrangThai(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(required = false) String loaiHoaDon
    ) {
        return thongKeService.thongKeTrangThai(from, to, loaiHoaDon);
    }

    @GetMapping("/top-san-pham")
    public List<TopProductResponse> topProducts(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String loaiHoaDon
    ) {
        return thongKeService.topProducts(from, to, limit, loaiHoaDon);
    }

    @GetMapping("/top-khach-hang")
    public List<TopCustomerResponse> topCustomers(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String loaiHoaDon
    ) {
        return thongKeService.topCustomers(from, to, limit, loaiHoaDon);
    }
}
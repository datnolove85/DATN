package com.example.backend.Service;

import com.example.backend.Response.thongke.*;

import java.time.LocalDate;
import java.util.List;

public interface ThongKeService {

    DashboardResponse dashboard(LocalDate from, LocalDate to, String loaiHoaDon);

    List<RevenueResponse> revenue(
            LocalDate from,
            LocalDate to,
            String groupBy,
            String loaiHoaDon
    );

    List<OrderStatusResponse> thongKeTrangThai(LocalDate from, LocalDate to, String loaiHoaDon);

    List<TopProductResponse> topProducts(LocalDate from, LocalDate to, Integer limit, String loaiHoaDon);

    List<TopCustomerResponse> topCustomers(LocalDate from, LocalDate to, Integer limit, String loaiHoaDon);
}
package com.example.backend.Controller;

import com.example.backend.Request.ShippingFeeRequest;
import com.example.backend.Service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ghn")
@CrossOrigin("*")
public class GhnController {

    @Autowired
    private GHNService ghnService;

    @PostMapping("/fee")
    public Integer fee(@RequestBody ShippingFeeRequest request){

        return ghnService.tinhPhiVanChuyen(
                request.getThanhPho(),
                request.getQuan(),
                request.getPhuong()
        );
    }

    @PostMapping("/shipping-info")
    public ResponseEntity<Map<String, Object>> getShippingInfo(@RequestBody ShippingFeeRequest request){
        // 1. Tính phí ship
        Integer phiShip = ghnService.tinhPhiVanChuyen(
                request.getThanhPho(),
                request.getQuan(),
                request.getPhuong()
        );

        // 2. Lấy timestamp thời gian giao dự kiến
        Long leadtimeTimestamp = ghnService.tinhThoiGianGiaoHang(
                request.getThanhPho(),
                request.getQuan(),
                request.getPhuong()
        );

        // 3. Format timestamp thành chuỗi ngày tháng dễ đọc (Ví dụ: "10/06/2026")
        String ngayGiaoDuKien = "Đang cập nhật";
        if (leadtimeTimestamp != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
            ngayGiaoDuKien = formatter.format(Instant.ofEpochSecond(leadtimeTimestamp));
        }

        // 4. Đóng gói kết quả trả về cho Frontend
        Map<String, Object> response = new HashMap<>();
        response.put("phi_ship", phiShip);
        response.put("ngay_giao_du_kien", ngayGiaoDuKien);

        return ResponseEntity.ok(response);
    }
}
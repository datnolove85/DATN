package com.example.backend.Controller;


import com.example.backend.Service.DonHangService;
import com.example.backend.secutity.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/don-hang")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DonHangController {


    private final DonHangService donHangService;

    private final JwtService jwtService;



    // ==================================================
    // LẤY DANH SÁCH ĐƠN HÀNG CỦA KHÁCH HÀNG
    // ==================================================

    @GetMapping
    public ResponseEntity<?> layDanhSachDonHang(
            HttpServletRequest request
    ){

        String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")){

            return ResponseEntity
                    .badRequest()
                    .body("Thiếu token");

        }


        String token = authHeader.substring(7);


        Integer idTaiKhoan = jwtService.extractId(token);



        return ResponseEntity.ok(
                donHangService.layDanhSachDonHang(idTaiKhoan)
        );

    }




    // ==================================================
    // CHI TIẾT ĐƠN HÀNG
    // ==================================================

    @GetMapping("/{idHoaDon}")
    public ResponseEntity<?> chiTietDonHang(

            @PathVariable Integer idHoaDon,

            HttpServletRequest request

    ){


        String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")){

            return ResponseEntity
                    .badRequest()
                    .body("Thiếu token");

        }



        String token = authHeader.substring(7);



        Integer idTaiKhoan = jwtService.extractId(token);



        return ResponseEntity.ok(
                donHangService.layChiTietDonHang(
                        idTaiKhoan,
                        idHoaDon
                )
        );


    }

}
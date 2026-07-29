package com.example.backend.Response;

import lombok.Data;

import java.util.List;

@Data
public class DonHangResponse {

    private DonHangInfoDTO thongTinDonHang;

    private NguoiNhanDTO nguoiNhan;

    private ThanhToanDTO thanhToan;

    private TheoDoiDonHangDTO theoDoi;

    private TraHangDTO traHang;

    private List<DonHangChiTietDTO> sanPham;
}
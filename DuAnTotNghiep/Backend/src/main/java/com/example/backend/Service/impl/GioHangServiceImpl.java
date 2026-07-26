package com.example.backend.Service.impl;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Request.ThemGioHangRequest;
import com.example.backend.Response.GioHangOnlineResponse;
import com.example.backend.Service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GioHangServiceImpl implements GioHangService {

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private HinhAnhRepository hinhAnhRepository;

    @Override
    @Transactional
    public void themSanPham(Integer idTaiKhoan, ThemGioHangRequest request) {

        // 1. Lấy khách hàng từ id tài khoản
        KhachHang khachHang = khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        // 2. Tìm giỏ hàng của khách
        GioHang gioHang = gioHangRepository
                .findByIdKhachHang_Id(khachHang.getId())
                .orElseGet(() -> {

                    GioHang gh = new GioHang();

                    gh.setIdKhachHang(khachHang);
                    gh.setNgayTao(LocalDateTime.now());
                    gh.setNgayCapNhat(LocalDateTime.now());
                    gh.setTrangThai(true);

                    return gioHangRepository.save(gh);
                });

        // 3. Kiểm tra sản phẩm đã có trong giỏ chưa
        Optional<GioHangChiTiet> optional = gioHangChiTietRepository
                .findByGioHang_IdAndSanPhamChiTiet_Id(
                        gioHang.getId(),
                        request.getIdSanPhamChiTiet()
                );

        if (optional.isPresent()) {

            // Nếu có thì cộng số lượng
            GioHangChiTiet ct = optional.get();

            ct.setSoLuong(ct.getSoLuong() + request.getSoLuong());

            gioHangChiTietRepository.save(ct);

        } else {
            // Nếu chưa có thì thêm mới
            GioHangChiTiet ct = new GioHangChiTiet();

            ct.setGioHang(gioHang);

            ct.setSanPhamChiTiet(
                    sanPhamChiTietRepository
                            .findById(request.getIdSanPhamChiTiet())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"))
            );

            ct.setSoLuong(request.getSoLuong());

            gioHangChiTietRepository.save(ct);
        }

        // 4. Cập nhật thời gian
        gioHang.setNgayCapNhat(LocalDateTime.now());

        gioHangRepository.save(gioHang);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GioHangOnlineResponse> layGioHang(Integer idTaiKhoan) {

        // 1. Tìm khách hàng theo tài khoản
        KhachHang khachHang = khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        // 2. Tìm giỏ hàng
        GioHang gioHang = gioHangRepository
                .findByIdKhachHang_Id(khachHang.getId())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng trống"));

        // 3. Lấy danh sách sản phẩm trong giỏ
        List<GioHangChiTiet> dsGioHang =
                gioHangChiTietRepository.findByGioHang_Id(gioHang.getId());

        // 4. Mapping Response
        return dsGioHang.stream().map(item -> {

            SanPhamChiTiet spct = item.getSanPhamChiTiet();

            GioHangOnlineResponse response = new GioHangOnlineResponse();

            HinhAnh anh = hinhAnhRepository
                    .findFirstByIdSanPhamChiTiet_IdAndLaAnhChinhTrue(spct.getId())
                    .orElse(null);

            if (anh != null) {
                response.setAnh("/sanpham/" + anh.getLink());
            }

            response.setId(item.getId());

            response.setIdSanPhamChiTiet(spct.getId());

            response.setMaSanPhamChiTiet(spct.getMaSanPhamChiTiet());

            response.setTenSanPham(spct.getIdSanPham().getTenSanPham());

            response.setThuongHieu(
                    spct.getIdSanPham()
                            .getIdThuongHieu()
                            .getTenThuongHieu()
            );

            response.setMauSac(spct.getIdMauSac().getTenMauSac());

            response.setKichCo(spct.getIdKichThuoc().getTenKichThuoc());

            response.setGiaBan(spct.getGiaBan());

            response.setSoLuongTon(spct.getSoLuongTon());

            response.setSoLuongKhaDung(spct.getSoLuongKhaDung());

            response.setSoLuong(item.getSoLuong());

            response.setThanhTien(
                    spct.getGiaBan()
                            .multiply(BigDecimal.valueOf(item.getSoLuong()))
            );

            return response;

        }).toList();
    }

    @Override
    @Transactional
    public void xoaSanPham(Integer idTaiKhoan, Integer idGioHangChiTiet) {

        // Tìm khách hàng
        KhachHang khachHang = khachHangRepository
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        // Tìm giỏ hàng
        GioHang gioHang = gioHangRepository
                .findByIdKhachHang_Id(khachHang.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));

        // Tìm sản phẩm trong giỏ
        GioHangChiTiet chiTiet = gioHangChiTietRepository
                .findById(idGioHangChiTiet)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ"));

        // Kiểm tra sản phẩm có thuộc giỏ của người dùng không
        if (!chiTiet.getGioHang().getId().equals(gioHang.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa sản phẩm này");
        }

        // Xóa
        gioHangChiTietRepository.delete(chiTiet);

        // Cập nhật thời gian
        gioHang.setNgayCapNhat(LocalDateTime.now());
        gioHangRepository.save(gioHang);
    }
}
package com.example.backend.Controller;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Entity.KhoVoucher;
import com.example.backend.Repository.NhanVienRepository;
import com.example.backend.Request.*;
import com.example.backend.Response.HoaDonResponse;
import com.example.backend.Response.VoucherKhachHangResponse;
import com.example.backend.Service.HoaDonService;
import com.example.backend.Service.TraHangService;
import com.example.backend.secutity.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/hoadon")
@RequiredArgsConstructor
public class HoaDonController {

    private final HoaDonService service;

    private final TraHangService traHangService;

    private final JwtService jwtService;

    private final NhanVienRepository nhanVienRepo;

    // ================= GET ALL =================
    @GetMapping
    public List<HoaDonResponse> getAll() {
        return service.getAll();
    }

    @PostMapping("/thanh-toan")
    public ResponseEntity<?> thanhToan(
            @RequestBody ThanhToanHoaDonRequest req,
            HttpServletRequest request // 1. Thêm HttpServletRequest để đọc Header
    ) {
        // 2. Lấy token từ header Authorization (giống hệt cách bạn đã làm)
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Thiếu hoặc sai định dạng Token xác thực!");
        }
        String token = authHeader.substring(7);

        // 3. Extract id tài khoản từ token, sau đó tìm id nhân viên tương ứng trong DB
        Integer idTaiKhoan = jwtService.extractId(token);
        Integer idNhanVien = nhanVienRepo.findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên ứng với tài khoản này"))
                .getId();

        // 4. Truyền thêm idNhanVien xuống tầng Service
        return ResponseEntity.ok(
                service.thanhToanHoaDon(req, idNhanVien)
        );
    }

    @PostMapping("/thanh-toan/online")
    public ResponseEntity<?> thanhToanOnline(
            @RequestBody ThanhToanHoaDonRequest req
    ) {

        return ResponseEntity.ok(
                service.thanhToanHoaDonOnline(req)
        );
    }
    @PostMapping("/search")
    public Page<HoaDon> search(
            @RequestBody HoaDonFilterRequest req,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("ngayTao").descending());

        return service.search(req, pageable);
    }

    @PostMapping("/online/search")
    public ResponseEntity<Page<HoaDonResponse>> searchOnline(
            @RequestBody HoaDonFilterRequest req,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("ngayTao").descending()
        );

        return ResponseEntity.ok(
                service.searchOnline(req, pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                service.getDetail(id)
        );
    }

    //Mới
    @GetMapping("/hoa-don-cho")
    public ResponseEntity<?> getHoaDonCho(Integer idNhanVien) {

        return ResponseEntity.ok(
                service.getHoaDonCho(idNhanVien)
        );
    }

    @PostMapping("/hoa-don-cho")
    public ResponseEntity<?> taoHoaDonCho(@RequestBody  TaoHoaDonRequest taoHoaDonRequest) {
        return ResponseEntity.ok(
                service.taoHoaDonCho(taoHoaDonRequest)
        );
    }

    @PostMapping("/online")
    public ResponseEntity<Map<String, Object>> createOnlineOrder(
            @RequestBody CreateOnlineOrderRequest req,
            HttpServletRequest request
    ) {

        return ResponseEntity.ok(
                service.createOnlineOrder(req, request)
        );
    }

    @PostMapping("online/{id}/cancel")
    public ResponseEntity<?> huyHoaDonOnline(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {

//        String token = request.getHeader("Authorization").substring(7);
//
//        Integer idTaiKhoan = jwtService.extractId(token);
//
//        Integer idNhanVien = nhanVienRepo
//                .findByIdTaiKhoan_Id(idTaiKhoan)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"))
//                .getId();

        service.huyHoaDonOnline(id);

        return ResponseEntity.ok("Hủy hóa đơn thành công");
    }

    @GetMapping("/{id}/chi-tiet")
    public ResponseEntity<?> getChiTietHoaDon(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                service.getChiTietHoaDon(id)
        );
    }

    @PutMapping("/chi-tiet/{id}/giam")
    public ResponseEntity<?> giamSoLuong(
            @PathVariable Integer id
    ) {

        service.giamSoLuong(id);

        return ResponseEntity.ok("OK");
    }

    @PostMapping("/them-san-pham")
    public ResponseEntity<?> themSanPham(
            @RequestBody ThemSanPhamRequest request
    ) {
        service.themSanPhamVaoHoaDon(
                request
        );

        return ResponseEntity.ok(
                "Thêm sản phẩm thành công"
        );
    }

    @PutMapping("/chi-tiet/{id}/tang")
    public ResponseEntity<?> tangSoLuong(
            @PathVariable Integer id
    ) {

        service.tangSoLuong(id);

        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/chi-tiet/{id}")
    public ResponseEntity<?> xoaSanPhamKhoiHoaDon(
            @PathVariable Integer id
    ) {

        service.xoaSanPhamKhoiHoaDon(id);

        return ResponseEntity.ok("OK");
    }

    @PutMapping("/gan-khach-hang")
    public ResponseEntity<?> ganKhachHang(@RequestBody GanKhachHangRequest request) {
        try {
            List<VoucherKhachHangResponse> danhSachVoucherCuaKhach =
                    service.ganKhachHang(
                            request.getIdHoaDon(),
                            request.getIdKhachHang()
                    );

            return ResponseEntity.ok(danhSachVoucherCuaKhach);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi gán khách hàng: " + e.getMessage());
        }
    }

    @PutMapping("/huy/{id}")
    public ResponseEntity<?> huyHoaDon(
            @PathVariable Integer id,
            HttpServletRequest request
    ) {

        String token = request.getHeader("Authorization").substring(7);

        Integer idTaiKhoan = jwtService.extractId(token);

        Integer idNhanVien = nhanVienRepo
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"))
                .getId();

        service.huyHoaDon(id, idNhanVien);

        return ResponseEntity.ok("Hủy hóa đơn thành công");
    }

    @DeleteMapping("/{idHoaDon}/voucher")
    public void boVoucher(
            @PathVariable Integer idHoaDon
    ){
        service.boVoucher(idHoaDon);
    }
    @PostMapping("/{idHoaDon}/voucher")
    public void apVoucher(
            @PathVariable Integer idHoaDon,
            @RequestParam(required = false) Integer idVoucher,
            @RequestParam(required = false) Integer idVoucherKhachHang
    ){
        service.apVoucher(
                idHoaDon,
                idVoucher,
                idVoucherKhachHang
        );
    }

    @GetMapping("/trahang/{id}")
    public ResponseEntity<?> getThongTinTraHang(@PathVariable Integer id) {
        return ResponseEntity.ok(
              traHangService.getThongTinTraHang(id)
        );
    }

    @PostMapping("/tra-hang")
    public ResponseEntity<?> traHang(@RequestBody TraHangRequest request) {

        traHangService.traHang(request);

        return ResponseEntity.ok("Trả hàng thành công");
    }

    @PatchMapping("/{id}/trang-thai")
    public ResponseEntity<?> updateTrangThai(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body,
            HttpServletRequest request
    ) {

        String token = request.getHeader("Authorization").substring(7);

        Integer idTaiKhoan = jwtService.extractId(token);

        Integer idNhanVien =nhanVienRepo
                .findByIdTaiKhoan_Id(idTaiKhoan)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"))
                .getId();

        service.updateTrangThai(id, body.get("trangThai"), idNhanVien);

        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    @PostMapping("/tao-qr/{hoaDonId}")
    public ResponseEntity<?> taoQr(
            @PathVariable Integer hoaDonId,
            @RequestParam(required = false) Long amount
    ) {
        return ResponseEntity.ok(service.taoQr(hoaDonId, amount));
    }

    @PutMapping("/cap-nhat-so-luong/{id}")
    public ResponseEntity<?> capNhatSoLuong(
            @PathVariable Integer id,
            @RequestParam Integer soLuong) {

        service.capNhatSoLuong(id, soLuong);

        return ResponseEntity.ok("OK");
    }
    @PutMapping("/{idHoaDon}/go-khach-hang")
    public ResponseEntity<List<KhoVoucher>> goKhachHang(@PathVariable Integer idHoaDon) {
        return ResponseEntity.ok(service.goKhachHang(idHoaDon));
    }

    @PostMapping("/{idHoaDon}/ap-dung-xu")
    public ResponseEntity<?> apDungXu(@PathVariable Integer idHoaDon, @RequestParam Integer soXu) {
        try {
            service.apDungXu(idHoaDon, soXu);
            return ResponseEntity.ok("Áp dụng xu thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
package com.example.backend.Controller;

import com.example.backend.Response.YeuThichResponseDTO;
import com.example.backend.Service.YeuThichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/yeu-thich")
@CrossOrigin(origins = "*")
public class YeuThichController {

    @Autowired
    private YeuThichService yeuThichService;

    /**
     * 1. Lấy danh sách sản phẩm yêu thích của một khách hàng (Có đầy đủ thông tin, giá, ảnh)
     * URL: GET /api/yeu-thich/{idKhachHang}
     */
    @GetMapping("/{idKhachHang}")
    public ResponseEntity<List<YeuThichResponseDTO>> getDanhSachYeuThich(@PathVariable Integer idKhachHang) {
        List<YeuThichResponseDTO> danhSach = yeuThichService.getDanhSachYeuThich(idKhachHang);
        return ResponseEntity.ok(danhSach);
    }

    /**
     * 2. Thả tim hoặc Bỏ tim sản phẩm (Toggle)
     * URL: POST /api/yeu-thich/toggle?idKhachHang=1&idSanPham=2
     * Trả về true nếu vừa thêm vào yêu thích, false nếu vừa bỏ yêu thích.
     */
    @PostMapping("/toggle")
    public ResponseEntity<?> toggleYeuThich(
            @RequestParam Integer idKhachHang,
            @RequestParam Integer idSanPham) {
        try {
            boolean trangThaiMoi = yeuThichService.toggleYeuThich(idKhachHang, idSanPham);
            String message = trangThaiMoi ? "Đã thêm vào danh sách yêu thích!" : "Đã xóa khỏi danh sách yêu thích!";

            return ResponseEntity.ok(new ApiResponse(true, message, trangThaiMoi));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    /**
     * 3. Kiểm tra xem khách hàng đã thích sản phẩm này chưa (dùng để sáng/tối icon trái tim ở giao diện)
     * URL: GET /api/yeu-thich/kiem-tra?idKhachHang=1&idSanPham=2
     */
    @GetMapping("/kiem-tra")
    public ResponseEntity<Boolean> kiemTraDaThich(
            @RequestParam Integer idKhachHang,
            @RequestParam Integer idSanPham) {
        boolean daThich = yeuThichService.kiemTraDaThich(idKhachHang, idSanPham);
        return ResponseEntity.ok(daThich);
    }

    // Class phụ trợ trả về cấu trúc JSON gọn gàng cho API phản hồi
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }
}
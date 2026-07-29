package com.example.backend.Config;

import com.example.backend.Entity.ChatLieu;
import com.example.backend.Entity.DanhMuc;
import com.example.backend.Entity.HinhAnh;
import com.example.backend.Entity.KhachHang;
import com.example.backend.Entity.KichThuoc;
import com.example.backend.Entity.MauSac;
import com.example.backend.Entity.NhanVien;
import com.example.backend.Entity.SanPham;
import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Entity.TaiKhoan;
import com.example.backend.Entity.ThuongHieu;
import com.example.backend.Entity.VaiTro;
import com.example.backend.Repository.ChatLieuRepository;
import com.example.backend.Repository.DanhMucRepository;
import com.example.backend.Repository.HinhAnhRepository;
import com.example.backend.Repository.KhachHangRepository;
import com.example.backend.Repository.KichThuocRepository;
import com.example.backend.Repository.MauSacRepository;
import com.example.backend.Repository.NhanVienRepository;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Repository.SanPhamRepository;
import com.example.backend.Repository.TaiKhoanRepository;
import com.example.backend.Repository.ThuongHieuRepository;
import com.example.backend.Repository.VaiTroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

/**
 * Local-only fixture set.  It mirrors the original SQL's catalog structure but
 * uses the current JPA schema, so it stays usable while the SQL schema evolves.
 */
@Component
@Profile("local")
@RequiredArgsConstructor
public class MockDataInitializer implements CommandLineRunner {

    private final VaiTroRepository vaiTroRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhanVienRepository nhanVienRepository;
    private final KhachHangRepository khachHangRepository;
    private final DanhMucRepository danhMucRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final ChatLieuRepository chatLieuRepository;
    private final MauSacRepository mauSacRepository;
    private final KichThuocRepository kichThuocRepository;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final HinhAnhRepository hinhAnhRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (sanPhamRepository.count() > 0) {
            return;
        }

        VaiTro adminRole = role("ADMIN", "ADMIN");
        VaiTro staffRole = role("NV", "STAFF");
        VaiTro customerRole = role("KH", "USERS");

        TaiKhoan admin = account(adminRole, "admin", "admin@stylehub.local", "0900000001");
        TaiKhoan staff = account(staffRole, "nhanvien01", "nv01@stylehub.local", "0900000002");
        TaiKhoan customer = account(customerRole, "khachhang01", "kh01@stylehub.local", "0900000003");

        NhanVien employee = new NhanVien();
        employee.setIdTaiKhoan(staff);
        employee.setMaNhanVien("NV001");
        employee.setTenNhanVien("Nguyen Van A");
        employee.setDiaChi("Ha Noi");
        employee.setNgaySinh(LocalDate.of(2000, 1, 1));
        employee.setGioiTinh(true);
        employee.setCanCuocCongDan("001234567890");
        employee.setNgayTao(Instant.now());
        employee.setTrangThai(1);
        employee = nhanVienRepository.save(employee);

        NhanVien administrator = new NhanVien();
        administrator.setIdTaiKhoan(admin);
        administrator.setMaNhanVien("NV002");
        administrator.setTenNhanVien("Quan tri vien");
        administrator.setDiaChi("Ha Noi");
        administrator.setNgaySinh(LocalDate.of(1998, 1, 1));
        administrator.setGioiTinh(true);
        administrator.setCanCuocCongDan("001234567891");
        administrator.setNgayTao(Instant.now());
        administrator.setTrangThai(1);
        nhanVienRepository.save(administrator);

        KhachHang buyer = new KhachHang();
        buyer.setIdTaiKhoan(customer);
        buyer.setMaKhachHang("KH001");
        buyer.setHoTen("Tran Thi B");
        buyer.setSoDienThoai("0911111111");
        buyer.setNgaySinh(LocalDate.of(2002, 5, 15));
        buyer.setHangThanhVien("BAC");
        buyer.setDiaChi("Ha Noi");
        buyer.setSoLanMua(3);
        buyer.setGioiTinh(false);
        buyer.setNgayTao(Instant.now());
        buyer.setTrangThai(false);
        khachHangRepository.save(buyer);

        DanhMuc aoThun = category("DM01", "Ao thun");
        DanhMuc aoSoMi = category("DM02", "Ao so mi");
        DanhMuc quanJean = category("DM03", "Quan jean");
        DanhMuc aoKhoac = category("DM04", "Ao khoac");

        ThuongHieu nike = brand("TH01", "Nike");
        ThuongHieu adidas = brand("TH02", "Adidas");
        ThuongHieu localBrand = brand("TH03", "Local Brand");
        ThuongHieu styleHub = brand("TH04", "StyleHub");

        ChatLieu cotton = material("CL01", "Cotton");
        ChatLieu jean = material("CL02", "Jean");
        ChatLieu kaki = material("CL03", "Kaki");
        ChatLieu ni = material("CL04", "Ni da ca");

        MauSac trang = color("MS01", "Trang");
        MauSac den = color("MS02", "Den");
        MauSac xanh = color("MS03", "Xanh");
        MauSac be = color("MS04", "Be");

        KichThuoc s = size("S", "S");
        KichThuoc m = size("M", "M");
        KichThuoc l = size("L", "L");
        KichThuoc xl = size("XL", "XL");

        SanPham tee = product("SP001", "Ao thun Nike Basic", "Ao thun cotton mem, form regular de phoi hang ngay.", aoThun, nike, cotton, employee);
        SanPham shirt = product("SP002", "Ao so mi Oxford", "Ao so mi vai cotton Oxford, phu hop di lam va di choi.", aoSoMi, adidas, cotton, employee);
        SanPham jeans = product("SP003", "Quan jean Slimfit", "Quan jean co gian nhe, form slimfit ton dang.", quanJean, localBrand, jean, employee);
        SanPham jacket = product("SP004", "Ao khoac ni Zip", "Ao khoac ni da ca day vua, co khoa keo tien dung.", aoKhoac, styleHub, ni, employee);

        variant(tee, trang, m, "SPCT001", 150_000, 300_000, 50, "anh1.jpg", true);
        variant(tee, den, l, "SPCT002", 150_000, 300_000, 40, "anh2.jpg", true);
        variant(tee, den, xl, "SPCT003", 150_000, 300_000, 18, "anh3.jpg", false);
        variant(shirt, trang, m, "SPCT004", 200_000, 420_000, 30, "anh4.jpg", true);
        variant(shirt, xanh, l, "SPCT005", 200_000, 420_000, 24, "anh5.jpg", true);
        variant(jeans, xanh, l, "SPCT006", 250_000, 520_000, 20, "anh6.jpg", true);
        variant(jeans, den, m, "SPCT007", 250_000, 520_000, 16, "anh1.jpg", true);
        variant(jacket, be, s, "SPCT008", 220_000, 450_000, 12, "anh2.jpg", true);
        variant(jacket, den, xl, "SPCT009", 220_000, 450_000, 8, "anh3.jpg", true);

        System.out.println("Local mock data loaded. Accounts: admin/admin123, nhanvien01/admin123, khachhang01/admin123");
    }

    private VaiTro role(String code, String name) {
        VaiTro value = new VaiTro();
        value.setMaVaiTro(code);
        value.setTenVaiTro(name);
        value.setTrangThai(1);
        return vaiTroRepository.save(value);
    }

    private TaiKhoan account(VaiTro role, String username, String email, String phone) {
        TaiKhoan value = new TaiKhoan();
        value.setIdVaiTro(role);
        value.setTenTaiKhoan(username);
        value.setEmail(email);
        value.setSoDienThoai(phone);
        value.setMatKhau("admin123");
        value.setNgayTao(Instant.now());
        value.setTrangThai(1);
        return taiKhoanRepository.save(value);
    }

    private DanhMuc category(String code, String name) {
        DanhMuc value = new DanhMuc();
        value.setMaDanhMuc(code);
        value.setTenDanhMuc(name);
        return danhMucRepository.save(value);
    }

    private ThuongHieu brand(String code, String name) {
        ThuongHieu value = new ThuongHieu();
        value.setMaThuongHieu(code);
        value.setTenThuongHieu(name);
        return thuongHieuRepository.save(value);
    }

    private ChatLieu material(String code, String name) {
        ChatLieu value = new ChatLieu();
        value.setMaChatLieu(code);
        value.setTenChatLieu(name);
        return chatLieuRepository.save(value);
    }

    private MauSac color(String code, String name) {
        MauSac value = new MauSac();
        value.setMaMauSac(code);
        value.setTenMauSac(name);
        return mauSacRepository.save(value);
    }

    private KichThuoc size(String code, String name) {
        KichThuoc value = new KichThuoc();
        value.setMaKichThuoc(code);
        value.setTenKichThuoc(name);
        value.setNgayTao(Instant.now());
        value.setNgayCapNhat(Instant.now());
        value.setTrangThai(true);
        return kichThuocRepository.save(value);
    }

    private SanPham product(String code, String name, String description, DanhMuc category, ThuongHieu brand,
                            ChatLieu material, NhanVien employee) {
        SanPham value = new SanPham();
        value.setMaSanPham(code);
        value.setTenSanPham(name);
        value.setMoTa(description);
        value.setIdDanhMuc(category);
        value.setIdThuongHieu(brand);
        value.setIdChatLieu(material);
        value.setIdNhanVienTao(employee);
        value.setIdNhanVienCapNhat(employee);
        value.setNgayTao(new Date());
        value.setNgayCapNhat(new Date());
        value.setTrangThai(true);
        return sanPhamRepository.save(value);
    }

    private void variant(SanPham product, MauSac color, KichThuoc size, String code, int cost, int price,
                         int stock, String imageName, boolean primary) {
        SanPhamChiTiet value = new SanPhamChiTiet();
        value.setIdSanPham(product);
        value.setIdMauSac(color);
        value.setIdKichThuoc(size);
        value.setMaSanPhamChiTiet(code);
        value.setTenSanPhamChiTiet(product.getTenSanPham() + " " + color.getTenMauSac() + " " + size.getTenKichThuoc());
        value.setGiaNhap(BigDecimal.valueOf(cost));
        value.setGiaBan(BigDecimal.valueOf(price));
        value.setSoLuongTon(stock);
        value.setSoLuongTamGiu(0);
        value.setTrangThai(true);
        value = sanPhamChiTietRepository.save(value);

        HinhAnh image = new HinhAnh();
        image.setIdSanPhamChiTiet(value);
        image.setTenAnh(imageName);
        image.setLink(imageName);
        image.setDinhDang("jpg");
        image.setLaAnhChinh(primary);
        image.setTrangThai(true);
        hinhAnhRepository.save(image);
    }
}

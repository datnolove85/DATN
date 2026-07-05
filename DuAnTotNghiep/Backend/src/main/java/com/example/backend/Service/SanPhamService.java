package com.example.backend.Service;

import com.example.backend.Entity.*;
import com.example.backend.Repository.*;
import com.example.backend.Request.SanPhamRequest;
import com.example.backend.Response.SanPhamChiTietResponse;
import com.example.backend.Response.SanPhamResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    public SanPham create(SanPhamRequest req) {

        DanhMuc danhMuc = danhMucRepository.findById(req.getIdDanhMuc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        ThuongHieu thuongHieu = thuongHieuRepository.findById(req.getIdThuongHieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));

        ChatLieu chatLieu = chatLieuRepository.findById(req.getIdChatLieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu"));

        SanPham sp = new SanPham();

        sp.setIdDanhMuc(danhMuc);
        sp.setIdThuongHieu(thuongHieu);
        sp.setIdChatLieu(chatLieu);

        sp.setTenSanPham(req.getTenSanPham());
        sp.setTrangThai(req.getTrangThai());
        sp.setMoTa(req.getMoTa());

        // Lưu lần đầu để sinh ID
        sp = sanPhamRepository.save(sp);

        // Sinh mã từ ID
        sp.setMaSanPham(String.format("SP%06d", sp.getId()));

        // Cập nhật lại mã
        return sanPhamRepository.save(sp);
    }

    // ================= UPDATE =================
    @Transactional
    public SanPham update(Integer id, SanPhamRequest req) {

        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        DanhMuc danhMuc = danhMucRepository.findById(req.getIdDanhMuc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        ThuongHieu thuongHieu = thuongHieuRepository.findById(req.getIdThuongHieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));

        ChatLieu chatLieu = chatLieuRepository.findById(req.getIdChatLieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu"));

        sp.setIdDanhMuc(danhMuc);
        sp.setIdThuongHieu(thuongHieu);
        sp.setIdChatLieu(chatLieu);
        sp.setTrangThai(req.getTrangThai());
        sp.setTenSanPham(req.getTenSanPham());
        sp.setMoTa(req.getMoTa());

        // Nếu SP ngừng kinh doanh
        if (req.getTrangThai() == false) {

            List<SanPhamChiTiet> dsSpct =
                    sanPhamChiTietRepository.findByIdSanPham_Id(id);

            dsSpct.forEach(spct -> spct.setTrangThai(false));

            sanPhamChiTietRepository.saveAll(dsSpct);
        }

        return sanPhamRepository.save(sp);
    }

    // ================= DELETE =================
    public void delete(Integer id) {
        SanPham sp = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        sp.setTrangThai(false);

        List<SanPhamChiTiet> dsSpct =
                sanPhamChiTietRepository.findByIdSanPham_Id(id);

        dsSpct.forEach(spct -> spct.setTrangThai(false));

        sanPhamChiTietRepository.saveAll(dsSpct);

        sanPhamRepository.save(sp);
    }

    // ================= GET =================
    public List<SanPhamResponse> getAll() {
        return sanPhamChiTietService.getAllSanPham();
    }

    public SanPham getById(Integer id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }


}
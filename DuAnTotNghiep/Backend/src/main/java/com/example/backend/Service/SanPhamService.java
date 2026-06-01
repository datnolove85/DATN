package com.example.backend.Service;

import com.example.backend.Entity.ChatLieu;
import com.example.backend.Entity.DanhMuc;
import com.example.backend.Entity.SanPham;
import com.example.backend.Entity.ThuongHieu;
import com.example.backend.Repository.ChatLieuRepository;
import com.example.backend.Repository.DanhMucRepository;
import com.example.backend.Repository.SanPhamRepository;
import com.example.backend.Repository.ThuongHieuRepository;
import com.example.backend.Request.SanPhamRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    // ================= CREATE =================
    public SanPham create(SanPhamRequest req) {

        SanPham sp = new SanPham();

        // ====== LOAD ENTITY FROM DB (CÁCH 2) ======
        DanhMuc danhMuc = danhMucRepository.findById(req.getIdDanhMuc())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        ThuongHieu thuongHieu = thuongHieuRepository.findById(req.getIdThuongHieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thương hiệu"));

        ChatLieu chatLieu = chatLieuRepository.findById(req.getIdChatLieu())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chất liệu"));

        // ====== SET ENTITY ======
        sp.setIdDanhMuc(danhMuc);
        sp.setIdThuongHieu(thuongHieu);
        sp.setIdChatLieu(chatLieu);

        sp.setMaSanPham(req.getMaSanPham());
        sp.setTenSanPham(req.getTenSanPham());
        sp.setMoTa(req.getMoTa());

        return sanPhamRepository.save(sp);
    }

    // ================= UPDATE =================
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

        sp.setMaSanPham(req.getMaSanPham());
        sp.setTenSanPham(req.getTenSanPham());
        sp.setMoTa(req.getMoTa());

        return sanPhamRepository.save(sp);
    }

    // ================= DELETE =================
    public void delete(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    // ================= GET =================
    public List<SanPham> getAll() {
        return sanPhamRepository.findAll();
    }

    public SanPham getById(Integer id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
    }
}
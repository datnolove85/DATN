package com.example.backend.Service;

import com.example.backend.Entity.HinhAnh;
import com.example.backend.Entity.SanPhamChiTiet;
import com.example.backend.Repository.HinhAnhRepository;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Response.HinhAnhResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class HinhAnhService {

    @Autowired
    HinhAnhRepository hinhAnhRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;
    public List<HinhAnh> getHinhAnh(){
        return hinhAnhRepository.findAll();
    }

    public void upload(Integer idSpct,
                       MultipartFile[] files)
            throws IOException {

        SanPhamChiTiet spct =
                sanPhamChiTietRepository.findById(idSpct)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy SPCT"));

        Path uploadPath = Paths.get("uploads/sanpham");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (MultipartFile file : files) {

            String fileName =
                    UUID.randomUUID()
                            + "_"
                            + file.getOriginalFilename();

            Files.copy(
                    file.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            HinhAnh img = new HinhAnh();

            img.setIdSanPhamChiTiet(spct);
            img.setTenAnh(file.getOriginalFilename());
            img.setLink(fileName);
            img.setDinhDang(file.getContentType());
            img.setLaAnhChinh(false);
            img.setTrangThai(true);

            hinhAnhRepository.save(img);
        }
    }

    public void delete(Integer id) {

        HinhAnh img =
                hinhAnhRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy ảnh"));

        img.setTrangThai(false);

        hinhAnhRepository.save(img);
    }

    public List<HinhAnhResponse> getBySPCT(Integer idSpct) {

        List<HinhAnh> list = hinhAnhRepository.findByIdSanPhamChiTiet_IdAndTrangThaiTrue(idSpct);

        return list.stream()
                .map(img -> new HinhAnhResponse(
                                img.getId(),
                                "/sanpham/" + img.getLink(),
                                img.getLaAnhChinh()
                ))
                .toList();
    }
    @Transactional
    public void datAnhChinh(Integer idAnh) {

        HinhAnh anh = hinhAnhRepository.findById(idAnh)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ảnh"));

        Integer idSPCT = anh.getIdSanPhamChiTiet().getId();

        // bỏ ảnh chính cũ
        hinhAnhRepository.boAnhChinh(idSPCT);

        // đặt ảnh mới
        anh.setLaAnhChinh(true);

        hinhAnhRepository.save(anh);
    }
}

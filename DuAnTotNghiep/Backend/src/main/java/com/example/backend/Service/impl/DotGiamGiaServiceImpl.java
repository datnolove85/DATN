package com.example.backend.Service.impl;


import com.example.backend.Entity.*;
import com.example.backend.Repository.DotGiamGiaRepository;
import com.example.backend.Repository.SanPhamChiTietRepository;
import com.example.backend.Repository.SanPhamGiamGiaRepository;
import com.example.backend.Repository.SanPhamRepository;
import com.example.backend.Request.CreateDotGiamGiaRequest;
import com.example.backend.Request.ThemSanPhamGGRequest;
import com.example.backend.Request.UpdateDotGiamGiaRequest;
import com.example.backend.Response.DotGiamGiaDetailResponse;
import com.example.backend.Response.DotGiamGiaResponse;
import com.example.backend.Response.SanPhamGiamGiaResponse;
import com.example.backend.Service.DotGiamGiaService;
import com.example.backend.Service.PosSocketService;
import com.example.backend.mapper.DotGiamGiaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DotGiamGiaServiceImpl implements DotGiamGiaService {

    private final DotGiamGiaRepository dotRepo;

    private final SanPhamGiamGiaRepository spggRepo;

    private final SanPhamChiTietRepository spctRepo;

    private final SanPhamRepository sanPhamRepository;

    private final PosSocketService posSocketService;

    //================================================
    // Danh sách
    //================================================

    @Override
    public List<DotGiamGiaResponse> getAll() {

        return dotRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(DotGiamGia::getNgayTao).reversed())
                .map(dot -> {

                    DotGiamGiaResponse response =
                            DotGiamGiaMapper.toResponse(
                                    dot,
                                    (int) spggRepo.countByDotGiamGiaId(dot.getId())
                            );


                    response.setTrangThai(
                            tinhTrangThai(dot)
                    );


                    return response;

                })
                .collect(Collectors.toList());

    }

    //================================================
    // Chi tiết
    //================================================

    @Override
    public DotGiamGiaDetailResponse getById(Integer id) {

        DotGiamGia dot = layTheoId(id);

        DotGiamGiaDetailResponse response =
                DotGiamGiaMapper.toDetailResponse(dot);


        response.setTrangThai(
                tinhTrangThai(dot)
        );


        return response;

    }

    //================================================
    // Tạo
    //================================================

    @Override
    public DotGiamGiaResponse create(CreateDotGiamGiaRequest request) {

        validateDate(
                request.getNgayBatDau(),
                request.getNgayKetThuc()
        );

        DotGiamGia dot = new DotGiamGia();

        dot.setMaDotGiamGia(taoMa());

        dot.setTenDotGiamGia(request.getTenDotGiamGia());

        dot.setLoaiGiamGia(request.getLoaiGiamGia());

        dot.setGiaTriGiam(request.getGiaTriGiam());

        dot.setGiaTriGiamToiDa(request.getGiaTriGiamToiDa());

        dot.setNgayBatDau(request.getNgayBatDau());

        dot.setNgayKetThuc(request.getNgayKetThuc());

        dot.setMoTa(request.getMoTa());

        dot.setNgayTao(Instant.now());

        dot.setNgayCapNhat(Instant.now());

        dot.setTrangThai(
                tinhTrangThai(
                        request.getNgayBatDau(),
                        request.getNgayKetThuc()
                )
        );

        dotRepo.save(dot);

        return DotGiamGiaMapper.toResponse(dot, 0);

    }

    //================================================
    // Cập nhật
    //================================================

    @Override
    public DotGiamGiaResponse update(Integer id,
                                     UpdateDotGiamGiaRequest request) {

        DotGiamGia dot = layTheoId(id);


        String trangThai = tinhTrangThai(dot);


        long soLuongSP =
                spggRepo.countByDotGiamGiaId(id);


        if ("dang_dien_ra".equals(trangThai)
                &&
                soLuongSP > 0) {

            throw new RuntimeException(
                    "Đợt giảm giá đang diễn ra và đã có sản phẩm áp dụng, không thể chỉnh sửa."
            );

        }


        validateDate(
                request.getNgayBatDau(),
                request.getNgayKetThuc()
        );

        dot.setTenDotGiamGia(request.getTenDotGiamGia());

        dot.setLoaiGiamGia(request.getLoaiGiamGia());

        dot.setGiaTriGiam(request.getGiaTriGiam());

        dot.setGiaTriGiamToiDa(request.getGiaTriGiamToiDa());

        dot.setNgayBatDau(request.getNgayBatDau());

        dot.setNgayKetThuc(request.getNgayKetThuc());

        dot.setMoTa(request.getMoTa());

        dot.setNgayCapNhat(Instant.now());

        if (!"tam_dung".equals(dot.getTrangThai())) {

            dot.setTrangThai(
                    tinhTrangThai(
                            request.getNgayBatDau(),
                            request.getNgayKetThuc()
                    )
            );

        }
        dotRepo.save(dot);

        return DotGiamGiaMapper.toResponse(
                dot,
                (int) spggRepo.countByDotGiamGiaId(dot.getId())
        );

    }

    //================================================
    // Xóa
    //================================================

    @Override
    public void delete(Integer id) {

        DotGiamGia dot = layTheoId(id);

        String trangThai = tinhTrangThai(dot);

        long soLuongSP =
                spggRepo.countByDotGiamGiaId(id);


        if ("dang_dien_ra".equals(trangThai)
                || "tam_dung".equals(trangThai)
                || "da_ket_thuc".equals(trangThai)) {

            throw new RuntimeException(
                    "Không thể xóa đợt giảm giá ở trạng thái hiện tại."
            );
        }


        if ("sap_dien_ra".equals(trangThai)
                && soLuongSP > 0) {

            throw new RuntimeException(
                    "Đợt giảm giá đã có sản phẩm áp dụng, không thể xóa."
            );
        }


        dotRepo.delete(dot);
    }
    //================================================
    // Đổi trạng thái
    //================================================

    @Override
    public void doiTrangThai(Integer id) {


        DotGiamGia dot = layTheoId(id);


        String trangThaiHienTai =
                tinhTrangThai(dot);


        if ("da_ket_thuc".equals(trangThaiHienTai)) {

            throw new RuntimeException(
                    "Đợt giảm giá đã kết thúc"
            );

        }


        if ("tam_dung".equals(dot.getTrangThai())) {


            dot.setTrangThai(
                    tinhTrangThai(
                            dot.getNgayBatDau(),
                            dot.getNgayKetThuc()
                    )
            );


        } else {


            dot.setTrangThai(
                    "tam_dung"
            );


        }


        dot.setNgayCapNhat(
                Instant.now()
        );


        dotRepo.save(dot);
        posSocketService.notifyDiscountUpdated();
    }

    //================================================
    // Private Method
    //================================================

    /**
     * Lấy theo id
     */
    private DotGiamGia layTheoId(Integer id) {

        return dotRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy đợt giảm giá"));

    }

    /**
     * Validate ngày
     */
    private void validateDate(
            Instant batDau,
            Instant ketThuc
    ) {

        if (batDau == null || ketThuc == null) {

            throw new RuntimeException("Ngày không được để trống.");

        }

        if (ketThuc.isBefore(batDau)) {

            throw new RuntimeException(
                    "Ngày kết thúc phải lớn hơn ngày bắt đầu."
            );

        }

    }

    /**
     * Sinh mã
     */
    private String taoMa() {

        long count = dotRepo.count() + 1;

        return String.format("DGG%05d", count);

    }

    /**
     * Tính trạng thái theo ngày
     */
    private String tinhTrangThai(
            Instant batDau,
            Instant ketThuc
    ) {

        Instant now = Instant.now();

        if (now.isBefore(batDau)) {

            return "sap_dien_ra";

        }

        if (now.isAfter(ketThuc)) {

            return "da_ket_thuc";

        }

        return "dang_dien_ra";

    }

    /**
     * Overload
     */
    private String tinhTrangThai(
            DotGiamGia dot
    ) {

        if ("tam_dung".equals(dot.getTrangThai())) {

            return "tam_dung";

        }

        return tinhTrangThai(
                dot.getNgayBatDau(),
                dot.getNgayKetThuc()
        );

    }

    //================================================
// Thêm sản phẩm vào đợt giảm giá
//================================================

    @Override
    @Transactional
    public List<SanPhamGiamGiaResponse> themSanPham(
            Integer idDot,
            ThemSanPhamGGRequest request
    ) {

        DotGiamGia dot =
                dotRepo.findById(idDot)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Không tìm thấy đợt giảm giá"
                                )
                        );


        List<SanPhamChiTiet> spcts =
                spctRepo.findAllById(
                        request.getIdsSanPhamChiTiet()
                );


        List<SanPhamGiamGia> list =
                new ArrayList<>();


        for (SanPhamChiTiet spct : spcts) {


            boolean exists =
                    spggRepo.existsByDotGiamGiaIdAndSanPhamChiTietId(
                            idDot,
                            spct.getId()
                    );


            if (exists) {
                continue;
            }


            SanPhamGiamGia spgg =
                    new SanPhamGiamGia();


            spgg.setDotGiamGia(dot);

            spgg.setSanPhamChiTiet(spct);


            list.add(spgg);

        }



        if (list.isEmpty()) {

            return new ArrayList<>();

        }



        List<SanPhamGiamGia> saved =
                spggRepo.saveAll(list);



        return saved.stream()
                .map(spgg -> {


                    SanPhamGiamGiaResponse rs =
                            new SanPhamGiamGiaResponse();



                    SanPhamChiTiet spct =
                            spgg.getSanPhamChiTiet();



                    rs.setId(
                            spgg.getId()
                    );



                    rs.setIdDotGiamGia(
                            dot.getId()
                    );



                    rs.setIdSanPhamChiTiet(
                            spct.getId()
                    );



                    rs.setIdSanPham(
                            spct.getIdSanPham()
                                    .getId()
                    );



                    rs.setTenSanPham(
                            spct.getIdSanPham()
                                    .getTenSanPham()
                    );



                    rs.setMaSanPham(
                            spct.getIdSanPham()
                                    .getMaSanPham()
                    );



                    rs.setGiaGoc(
                            spct.getGiaBan()
                    );



                    rs.setSoLuongTon(
                            spct.getSoLuongTon()
                    );



                    if(spct.getIdMauSac()!=null){

                        rs.setMauSac(
                                spct.getIdMauSac()
                                        .getTenMauSac()
                        );

                    }



                    if(spct.getIdKichThuoc()!=null){

                        rs.setKichThuoc(
                                spct.getIdKichThuoc()
                                        .getTenKichThuoc()
                        );

                    }

                    posSocketService.notifyDiscountUpdated();

                    return rs;

                })
                .toList();


    }
    //================================================
// Xóa sản phẩm khỏi đợt
//================================================

    @Override
    public void xoaSanPham(Integer idDot,
                           Integer idSPCT){
        spggRepo.deleteByDotGiamGiaIdAndSanPhamChiTietId(
                idDot,
                idSPCT
        );
        posSocketService.notifyDiscountUpdated();
    }
    //================================================
// Danh sách sản phẩm trong đợt
//================================================

    @Override
    public List<SanPhamGiamGiaResponse> getSanPham(Integer idDot) {


        DotGiamGia dot =
                layTheoId(idDot);


        List<SanPhamGiamGia> list =
                spggRepo.findByDotGiamGiaId(idDot);



        return list.stream()
                .map(spgg -> mapSPCTDangGiamGia(spgg, dot))
                .toList();

    }
    private SanPhamGiamGiaResponse mapSPCTDangGiamGia(
            SanPhamGiamGia spgg,
            DotGiamGia dot
    ){

        SanPhamChiTiet spct =
                spgg.getSanPhamChiTiet();


        SanPham sp =
                spct.getIdSanPham();



        SanPhamGiamGiaResponse rs =
                new SanPhamGiamGiaResponse();

        if (sp.getIdThuongHieu() != null) {
            rs.setThuongHieu(
                    sp.getIdThuongHieu().getTenThuongHieu()
            );
        }

        if (sp.getIdChatLieu() != null) {
            rs.setChatLieu(
                    sp.getIdChatLieu().getTenChatLieu()
            );
        }

        rs.setId(
                spgg.getId()
        );


        rs.setIdDotGiamGia(
                dot.getId()
        );

        rs.setIdSanPham(
                sp.getId()
        );

        rs.setIdSanPhamChiTiet(
                spct.getId()
        );
        rs.setMaSPCT(spct.getMaSanPhamChiTiet());


        rs.setTenSanPham(
                sp.getTenSanPham()
        );


        rs.setMaSanPham(
                sp.getMaSanPham()
        );


        rs.setGiaGoc(
                spct.getGiaBan()
        );


        rs.setPhanTramGiam(
                dot.getGiaTriGiam()
        );



        BigDecimal giaSauGiam =
                spct.getGiaBan();



        if("phan_tram".equals(dot.getLoaiGiamGia())){


            BigDecimal tienGiam =
                    spct.getGiaBan()
                            .multiply(
                                    dot.getGiaTriGiam()
                                            .divide(
                                                    BigDecimal.valueOf(100)
                                            )
                            );



            if(dot.getGiaTriGiamToiDa()!=null
                    &&
                    tienGiam.compareTo(
                            dot.getGiaTriGiamToiDa()
                    ) > 0
            ){

                tienGiam =
                        dot.getGiaTriGiamToiDa();

            }



            giaSauGiam =
                    spct.getGiaBan()
                            .subtract(tienGiam);


        }
        else if("tien_mat".equals(dot.getLoaiGiamGia())){


            giaSauGiam =
                    spct.getGiaBan()
                            .subtract(
                                    dot.getGiaTriGiam()
                            );

        }



        if(giaSauGiam.compareTo(BigDecimal.ZERO)<0){

            giaSauGiam =
                    BigDecimal.ZERO;

        }


        rs.setGiaSauGiam(
                giaSauGiam
        );


        rs.setSoLuongTon(
                spct.getSoLuongTon()
        );



        // màu
        if(spct.getIdMauSac()!=null){

            rs.setMauSac(
                    spct.getIdMauSac()
                            .getTenMauSac()
            );

        }


        // size
        if(spct.getIdKichThuoc()!=null){

            rs.setKichThuoc(
                    spct.getIdKichThuoc()
                            .getTenKichThuoc()
            );

        }



        List<String> anh =
                spctRepo.findLinkAnhChinhBySanPhamChiTietId(
                        spct.getId()
                );


        if(!anh.isEmpty()){

            rs.setAnh(
                    "/sanpham/" + anh.get(0)
            );

        }


        return rs;

    }



    @Override
    public List<SanPhamGiamGiaResponse> getSanPhamChuaApDung(Integer idDot) {

        DotGiamGia dot = layTheoId(idDot);

        return spctRepo.findSanPhamChuaApDung()
                .stream()
                .map(spct -> mapSPCTChuaGiamGia(spct, dot))
                .toList();
    }

        private SanPhamGiamGiaResponse mapSPCTChuaGiamGia(
                SanPhamChiTiet spct,
                DotGiamGia dot
        ){

            SanPhamGiamGiaResponse rs =
                    new SanPhamGiamGiaResponse();



            SanPham sp =
                    spct.getIdSanPham();

            if (sp.getIdThuongHieu() != null) {
                rs.setThuongHieu(
                        sp.getIdThuongHieu().getTenThuongHieu()
                );
            }

            if (sp.getIdChatLieu() != null) {
                rs.setChatLieu(
                        sp.getIdChatLieu().getTenChatLieu()
                );
            }

            rs.setIdDotGiamGia(
                    dot.getId()
            );

            rs.setIdSanPhamChiTiet(
                    spct.getId()
            );

            rs.setMaSPCT(spct.getMaSanPhamChiTiet());

            rs.setIdSanPham(
                    sp.getId()
            );


            rs.setMaSanPham(
                    sp.getMaSanPham()
            );


            rs.setTenSanPham(
                    sp.getTenSanPham()
            );


            rs.setGiaGoc(
                    spct.getGiaBan()
            );


            rs.setSoLuongTon(
                    spct.getSoLuongTon()
            );



            if(spct.getIdMauSac()!=null){

                rs.setMauSac(
                        spct.getIdMauSac()
                                .getTenMauSac()
                );

            }



            if(spct.getIdKichThuoc()!=null){

                rs.setKichThuoc(
                        spct.getIdKichThuoc()
                                .getTenKichThuoc()
                );

            }




            BigDecimal giaSauGiam =
                    spct.getGiaBan();



            if("phan_tram".equals(dot.getLoaiGiamGia())){


                BigDecimal tienGiam =
                        spct.getGiaBan()
                                .multiply(
                                        dot.getGiaTriGiam()
                                                .divide(
                                                        BigDecimal.valueOf(100)
                                                )
                                );



                if(dot.getGiaTriGiamToiDa()!=null
                        &&
                        tienGiam.compareTo(
                                dot.getGiaTriGiamToiDa()
                        )>0
                ){

                    tienGiam =
                            dot.getGiaTriGiamToiDa();

                }



                giaSauGiam =
                        spct.getGiaBan()
                                .subtract(tienGiam);


                rs.setPhanTramGiam(
                        dot.getGiaTriGiam()
                );

            }
            else if("tien_mat".equals(dot.getLoaiGiamGia())){


                giaSauGiam =
                        spct.getGiaBan()
                                .subtract(
                                        dot.getGiaTriGiam()
                                );

            }



            if(giaSauGiam.compareTo(BigDecimal.ZERO)<0){

                giaSauGiam =
                        BigDecimal.ZERO;

            }



            rs.setGiaSauGiam(
                    giaSauGiam
            );



            List<String> anh =
                    spctRepo.findLinkAnhChinhBySanPhamChiTietId(
                            spct.getId()
                    );


            if(!anh.isEmpty()){

                rs.setAnh(
                        "/sanpham/" + anh.get(0)
                );

            }



            return rs;

        }
}
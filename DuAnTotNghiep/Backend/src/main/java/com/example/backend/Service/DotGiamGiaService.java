package com.example.backend.Service;

import com.example.backend.Request.CreateDotGiamGiaRequest;
import com.example.backend.Request.ThemSanPhamGGRequest;
import com.example.backend.Request.ThemSanPhamRequest;
import com.example.backend.Request.UpdateDotGiamGiaRequest;
import com.example.backend.Response.DotGiamGiaDetailResponse;
import com.example.backend.Response.DotGiamGiaResponse;
import com.example.backend.Response.SanPhamGiamGiaResponse;

import java.util.List;

public interface DotGiamGiaService {

    /**
     * Danh sách đợt giảm giá
     */
    List<DotGiamGiaResponse> getAll();

    /**
     * Chi tiết đợt giảm giá
     */
    DotGiamGiaDetailResponse getById(Integer id);

    /**
     * Tạo đợt giảm giá
     */
    DotGiamGiaResponse create(CreateDotGiamGiaRequest request);

    /**
     * Cập nhật
     */
    DotGiamGiaResponse update(Integer id,
                              UpdateDotGiamGiaRequest request);

    /**
     * Xóa
     */
    void delete(Integer id);

    /**
     * Thêm sản phẩm vào đợt
     */
    List<SanPhamGiamGiaResponse> themSanPham(Integer idDot,
                     ThemSanPhamGGRequest request);

    /**
     * Xóa sản phẩm khỏi đợt
     */
    void xoaSanPham(Integer idDot,
                    Integer idSPCT);

    /**
     * Danh sách sản phẩm trong đợt
     */
    List<SanPhamGiamGiaResponse> getSanPham(Integer idDot);

    /**
     * Danh sách sản phẩm chưa áp dụng
     */
    List<SanPhamGiamGiaResponse> getSanPhamChuaApDung(Integer idDot);

    /**
     * Đổi trạng thái
     */
    void doiTrangThai(Integer id);

}
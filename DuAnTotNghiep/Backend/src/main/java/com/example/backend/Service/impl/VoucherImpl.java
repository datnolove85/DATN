package com.example.backend.Service.impl;

import com.example.backend.Entity.Voucher;
import com.example.backend.Repository.VoucherRepository;
import com.example.backend.Request.VoucherRequest;
import com.example.backend.Response.VoucherResponse;
import com.example.backend.Service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoucherImpl implements VoucherService {
    private static final String LOAI_PHAN_TRAM = "phan_tram";
    private static final String LOAI_TIEN_MAT = "tien_mat";

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<VoucherResponse> phanTrangVoucher(Integer pageNo, Integer pageSize) {
        int currentPage = pageNo == null || pageNo < 0 ? 0 : pageNo;
        int currentSize = pageSize == null || pageSize <= 0 ? 5 : pageSize;
        return voucherRepository.phanTrang(PageRequest.of(currentPage, currentSize));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VoucherResponse> searchVoucher(String keyword) {
        return voucherRepository.search(keyword == null ? "" : keyword.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherResponse detailVoucher(Integer id) {
        requirePositiveId(id, "ID voucher");
        return voucherRepository.detail(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher với ID: " + id));
    }

    @Override
    @Transactional
    public VoucherResponse addVoucher(VoucherRequest voucherRequest) {
        validate(voucherRequest, null);

        Voucher voucher = new Voucher();
        mapRequest(voucherRequest, voucher);
        voucher.setNgayTao(LocalDateTime.now());
        voucher.setNgayCapNhat(LocalDateTime.now());

        return toResponse(voucherRepository.save(voucher));
    }

    @Override
    @Transactional
    public VoucherResponse updateVoucher(Integer id, VoucherRequest voucherRequest) {
        requirePositiveId(id, "ID voucher");
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher với ID: " + id));

        validate(voucherRequest, id);
        mapRequest(voucherRequest, voucher);
        voucher.setNgayCapNhat(LocalDateTime.now());

        return toResponse(voucherRepository.save(voucher));
    }

    @Override
    @Transactional
    public void deleteVoucher(Integer id) {
        requirePositiveId(id, "ID voucher");
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher với ID: " + id));
        voucher.setTrangThai(0);
        voucher.setNgayCapNhat(LocalDateTime.now());
        voucherRepository.save(voucher);
    }

    private void mapRequest(VoucherRequest request, Voucher voucher) {
        voucher.setMaVoucher(request.getMaVoucher().trim().toLowerCase());
        voucher.setTenVoucher(request.getTenVoucher().trim());
        voucher.setLoaiGiamGia(request.getLoaiGiamGia().trim());
        voucher.setGiaTriGiam(request.getGiaTriGiam());
        voucher.setGiaTriDonHangToiThieu(defaultMoney(request.getGiaTriDonHangToiThieu()));
        voucher.setGiaTriGiamToiDa(request.getGiaTriGiamToiDa());
        voucher.setSoLuong(defaultInt(request.getSoLuong()));
        voucher.setSoLuongDaDung(defaultInt(request.getSoLuongDaDung()));
        voucher.setNgayBatDau(request.getNgayBatDau());
        voucher.setNgayKetThuc(request.getNgayKetThuc());
        voucher.setMoTa(request.getMoTa());
        voucher.setTrangThai(request.getTrangThai() == null ? 1 : request.getTrangThai());
    }

    private void validate(VoucherRequest request, Integer currentId) {
        if (request == null) {
            throw new RuntimeException("Dữ liệu voucher không được để trống");
        }
        if (isBlank(request.getMaVoucher())) {
            throw new RuntimeException("Mã voucher không được để trống");
        }
        if (request.getMaVoucher().trim().length() > 30) {
            throw new RuntimeException("Mã voucher không được vượt quá 30 ký tự");
        }
        if (isBlank(request.getTenVoucher())) {
            throw new RuntimeException("Tên voucher không được để trống");
        }
        if (request.getTenVoucher().trim().length() > 200) {
            throw new RuntimeException("Tên voucher không được vượt quá 200 ký tự");
        }

        voucherRepository.findByMaVoucherIgnoreCase(request.getMaVoucher().trim()).ifPresent(existing -> {
            if (currentId == null || !existing.getId().equals(currentId)) {
                throw new RuntimeException("Mã voucher đã tồn tại");
            }
        });

        String loaiGiamGia = request.getLoaiGiamGia() == null ? "" : request.getLoaiGiamGia().trim();
        if (!LOAI_PHAN_TRAM.equals(loaiGiamGia) && !LOAI_TIEN_MAT.equals(loaiGiamGia)) {
            throw new RuntimeException("Loại giảm giá chỉ được là phan_tram hoặc tien_mat");
        }

        if (request.getGiaTriGiam() == null || request.getGiaTriGiam().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Giá trị giảm phải lớn hơn 0");
        }
        if (LOAI_PHAN_TRAM.equals(loaiGiamGia) && request.getGiaTriGiam().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new RuntimeException("Giá trị giảm theo phần trăm không được vượt quá 100");
        }
        if (request.getGiaTriDonHangToiThieu() != null && request.getGiaTriDonHangToiThieu().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Giá trị đơn hàng tối thiểu không được âm");
        }
        if (request.getGiaTriGiamToiDa() != null && request.getGiaTriGiamToiDa().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Giá trị giảm tối đa không được âm");
        }
        if (request.getSoLuong() != null && request.getSoLuong() < 0) {
            throw new RuntimeException("Số lượng voucher không được âm");
        }
        if (request.getSoLuongDaDung() != null && request.getSoLuongDaDung() < 0) {
            throw new RuntimeException("Số lượng đã dùng không được âm");
        }
        if (defaultInt(request.getSoLuongDaDung()) > defaultInt(request.getSoLuong())) {
            throw new RuntimeException("Số lượng đã dùng không được lớn hơn số lượng voucher");
        }
        if (request.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống");
        }
        if (request.getNgayKetThuc() == null) {
            throw new RuntimeException("Ngày kết thúc không được để trống");
        }
        if (!request.getNgayBatDau().isBefore(request.getNgayKetThuc())) {
            throw new RuntimeException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        if (request.getTrangThai() != null && request.getTrangThai() != 0 && request.getTrangThai() != 1) {
            throw new RuntimeException("Trạng thái voucher chỉ được là 0 hoặc 1");
        }
    }

    private VoucherResponse toResponse(Voucher voucher) {
        return new VoucherResponse(
                voucher.getId(),
                voucher.getMaVoucher(),
                voucher.getTenVoucher(),
                voucher.getLoaiGiamGia(),
                voucher.getGiaTriGiam(),
                voucher.getGiaTriDonHangToiThieu(),
                voucher.getGiaTriGiamToiDa(),
                voucher.getSoLuong(),
                voucher.getSoLuongDaDung(),
                voucher.getNgayBatDau(),
                voucher.getNgayKetThuc(),
                voucher.getMoTa(),
                voucher.getNgayTao(),
                voucher.getNgayCapNhat(),
                voucher.getTrangThai()
        );
    }

    private Integer requirePositiveId(Integer id, String fieldName) {
        if (id == null || id <= 0) {
            throw new RuntimeException(fieldName + " không hợp lệ");
        }
        return id;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private BigDecimal defaultMoney(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }

    @Override
    public List<Voucher> getAll(){
        return voucherRepository.findAll();
    }
}
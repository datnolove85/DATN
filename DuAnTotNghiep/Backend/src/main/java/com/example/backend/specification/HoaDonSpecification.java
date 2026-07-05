package com.example.backend.specification;

import com.example.backend.Entity.HoaDon;
import com.example.backend.Request.HoaDonFilterRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class HoaDonSpecification {

    public static Specification<HoaDon> filter(HoaDonFilterRequest req) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // keyword
            if (req.getKeyword() != null && !req.getKeyword().isEmpty()) {
                String kw = "%" + req.getKeyword().toLowerCase() + "%";

                Predicate code = cb.like(cb.lower(root.get("maHoaDon")), kw);
                Predicate name = cb.like(cb.lower(root.get("tenNguoiNhan")), kw);
                Predicate phone = cb.like(root.get("soDienThoaiNguoiNhan"), kw);

                predicates.add(cb.or(code, name, phone));
            }

            // trạng thái
            if (req.getTrangThai() != null && !req.getTrangThai().isEmpty()) {
                predicates.add(cb.equal(root.get("trangThai"), req.getTrangThai()));
            }
            // trạng thái thanh toán
            if (req.getTrangThaiThanhToan() != null && !req.getTrangThaiThanhToan().isBlank()) {
                predicates.add(
                        cb.like(
                                root.get("trangThaiThanhToan").as(String.class),
                                "%" + req.getTrangThaiThanhToan() + "%"
                        )
                );
            }
            // loại hóa đơn
            if (req.getLoaiHoaDon() != null && !req.getLoaiHoaDon().isEmpty()) {
                predicates.add(cb.equal(root.get("loaiHoaDon"), req.getLoaiHoaDon()));
            }

            // giá
            if (req.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("tongThanhToan"), req.getMinPrice()));
            }

            if (req.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("tongThanhToan"), req.getMaxPrice()));
            }

            // from date
            if (req.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("ngayTao"), req.getFromDate().atStartOfDay()));
            }

            // to date
            if (req.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("ngayTao"), req.getToDate().atTime(23,59,59)));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

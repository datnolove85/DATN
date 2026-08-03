    package com.example.backend.Entity; // Thay đổi package cho phù hợp với dự án của bạn

    import jakarta.persistence.*;
    import java.time.LocalDateTime;

    @Entity
    @Table(name = "voucher_cua_khach_hang")
    public class VoucherCuaKhachHang {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "id_khach_hang", nullable = false)
        private Integer idKhachHang;

        @Column(name = "id_kho_voucher", nullable = false)
        private Integer idKhoVoucher;

        @Column(name = "trang_thai")
        private String trangThai;

        @Column(name = "ngay_doi")
        private LocalDateTime ngayDoi;

        // Constructors
        public VoucherCuaKhachHang() {}

        // Getters and Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public Integer getIdKhachHang() { return idKhachHang; }
        public void setIdKhachHang(Integer idKhachHang) { this.idKhachHang = idKhachHang; }

        public Integer getIdKhoVoucher() { return idKhoVoucher; }
        public void setIdKhoVoucher(Integer idKhoVoucher) { this.idKhoVoucher = idKhoVoucher; }

        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

        public LocalDateTime getNgayDoi() { return ngayDoi; }
        public void setNgayDoi(LocalDateTime ngayDoi) { this.ngayDoi = ngayDoi; }
    }
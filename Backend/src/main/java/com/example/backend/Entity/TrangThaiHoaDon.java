package com.example.backend.Entity;

public enum TrangThaiHoaDon {

    CHO_XAC_NHAN("cho_xac_nhan"),

    DA_XAC_NHAN("da_xac_nhan"),

    CHO_VAN_CHUYEN("cho_van_chuyen"),

    DANG_GIAO("dang_giao"),

    GIAO_THANH_CONG("giao_thanh_cong"),

    GIAO_THAT_BAI("giao_that_bai"),

    HOAN_THANH("hoan_thanh"),

    DA_HUY("da_huy");

    private final String value;

    TrangThaiHoaDon(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TrangThaiHoaDon fromValue(String value) {
        for (TrangThaiHoaDon tt : values()) {
            if (tt.value.equalsIgnoreCase(value)) {
                return tt;
            }
        }
        throw new IllegalArgumentException("Trạng thái không hợp lệ: " + value);
    }
}
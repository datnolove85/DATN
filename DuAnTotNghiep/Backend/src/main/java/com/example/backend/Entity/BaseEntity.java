package com.example.backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @Column(name = "ngay_cap_nhat")
    private Instant ngayCapNhat;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        ngayTao = now;
        ngayCapNhat = now;

        if (trangThai == null) {
            trangThai = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        ngayCapNhat = Instant.now();
    }
}
package com.example.backend.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "mau_sac")
public class MauSac extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @Column(name = "ma_mau_sac", nullable = false, length = 20)
    private String maMauSac;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "ten_mau_sac", nullable = false, length = 100)
    private String tenMauSac;

}
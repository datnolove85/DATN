package com.example.backend.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "settings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Setting {
    @Id
    private String settingKey;  // Ví dụ: CHE_DO_VUOT_NGAY
    private String settingValue; // Ví dụ: LAP_LAI hoặc GIU_NGAY_CUOI
}
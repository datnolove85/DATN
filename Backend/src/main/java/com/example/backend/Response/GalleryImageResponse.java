package com.example.backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GalleryImageResponse {

    private Integer spctId;

    private Integer mauSacId;

    private Integer kichThuocId;

    private String image;
}
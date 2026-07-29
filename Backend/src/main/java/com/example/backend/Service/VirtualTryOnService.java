package com.example.backend.Service;

import com.example.backend.Response.VirtualTryOnResult;
import org.springframework.web.multipart.MultipartFile;

public interface VirtualTryOnService {

    VirtualTryOnResult tryOn(
            Integer sanPhamChiTietId,
            MultipartFile personImage,
            String category
    );
}

package com.example.backend.Controller;

import com.example.backend.Exception.VirtualTryOnException;
import com.example.backend.Response.VirtualTryOnResult;
import com.example.backend.Service.VirtualTryOnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/virtual-try-on")
@RequiredArgsConstructor
public class VirtualTryOnController {

    private final VirtualTryOnService virtualTryOnService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> tryOn(
            @RequestParam("spctId") Integer sanPhamChiTietId,
            @RequestParam("personImage") MultipartFile personImage,
            @RequestParam(value = "category", defaultValue = "upper_body") String category
    ) {
        try {
            VirtualTryOnResult result = virtualTryOnService.tryOn(
                    sanPhamChiTietId,
                    personImage,
                    category
            );

            MediaType mediaType;
            try {
                mediaType = MediaType.parseMediaType(result.contentType());
            } catch (Exception ignored) {
                mediaType = MediaType.IMAGE_PNG;
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CACHE_CONTROL, "no-store")
                    .contentType(mediaType)
                    .body(result.imageBytes());
        } catch (VirtualTryOnException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}

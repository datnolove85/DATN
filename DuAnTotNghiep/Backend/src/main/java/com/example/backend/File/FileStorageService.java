package com.example.backend.File;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Path UPLOAD_DIR =
            Paths.get("src/main/resources/static/sanpham");

    public String saveFile(MultipartFile file) throws IOException {

        if (!Files.exists(UPLOAD_DIR)) {
            Files.createDirectories(UPLOAD_DIR);
        }

        String fileName =
                UUID.randomUUID() + "_" + file.getOriginalFilename();

        Files.copy(
                file.getInputStream(),
                UPLOAD_DIR.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
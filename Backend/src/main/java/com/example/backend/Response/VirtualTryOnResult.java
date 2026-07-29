package com.example.backend.Response;

public record VirtualTryOnResult(
        byte[] imageBytes,
        String contentType
) {
}

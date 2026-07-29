package com.example.backend.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatbotHistoryItem(
        @NotBlank @Size(max = 16) String role,
        @NotBlank @Size(max = 2000) String text
) {
}

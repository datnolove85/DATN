package com.example.backend.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ChatbotMessageRequest(
        @NotBlank(message = "Tin nhan khong duoc de trong")
        @Size(max = 2000, message = "Tin nhan toi da 2000 ky tu") String message,
        @Size(max = 12, message = "Lich su hoi thoai toi da 12 tin nhan")
        List<@Valid ChatbotHistoryItem> history
) {
}

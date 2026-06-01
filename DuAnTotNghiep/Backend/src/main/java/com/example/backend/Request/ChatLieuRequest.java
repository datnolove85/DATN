package com.example.backend.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatLieuRequest {

    @NotBlank
    private String maChatLieu;

    @NotBlank
    private String tenChatLieu;

    private Boolean trangThai;
}
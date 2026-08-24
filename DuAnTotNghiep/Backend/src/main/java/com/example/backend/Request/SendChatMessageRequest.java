package com.example.backend.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendChatMessageRequest {
    @NotBlank
    private String noiDung;
    private String loai = "TEXT";
}

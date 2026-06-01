package com.example.backend.Exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@AllArgsConstructor
@Data
@Builder
public class ApiException extends RuntimeException {

    private String code;

    public ApiException(String message, String code) {
        super(message);
        this.code = code;
    }


}

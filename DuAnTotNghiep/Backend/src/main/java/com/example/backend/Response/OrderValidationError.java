package com.example.backend.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderValidationError {

    private Integer productId;

    private Integer productDetailId;

    private String type;

    private String message;

}
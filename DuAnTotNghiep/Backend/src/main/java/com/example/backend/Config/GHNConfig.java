package com.example.backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Getter
@Configuration
public class GHNConfig {

    @Value("${ghn.token}")
    private String token;

    @Value("${ghn.shop-id}")
    private Integer shopId;
}
package com.example.backend.utils;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class VietQrUtil {

    // Thay bằng ngân hàng của bạn
    private static final String BANK_CODE = "970407";

    // Thay bằng STK của bạn
    private static final String ACCOUNT_NO = "7208052006";

    public static String createQrUrl(Long amount, String content) {

        String encodedContent =
                URLEncoder.encode(content, StandardCharsets.UTF_8);

        return String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%d&addInfo=%s",
                BANK_CODE,
                ACCOUNT_NO,
                amount,
                encodedContent
        );
    }

}
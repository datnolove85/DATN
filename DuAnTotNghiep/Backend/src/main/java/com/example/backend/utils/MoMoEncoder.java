//package com.example.backend.utils;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//
//public class MoMoEncoder {
//    public static String hmacSha256(String data, String key) {
//        try {
//            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//            sha256_HMAC.init(secret_key);
//            byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
//
//            StringBuilder result = new StringBuilder();
//            for (byte b : hash) {
//                result.append(String.format("%02x", b)); // Bắt buộc là %02x (chữ thường)
//            }
//            return result.toString();
//        } catch (Exception e) {
//            throw new RuntimeException("Lỗi băm HMAC-SHA256: " + e.getMessage());
//        }
//    }
//}
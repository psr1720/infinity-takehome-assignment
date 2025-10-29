package com.example.infinity_webhook_service.validators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class SignatureValidator {
    @Value("${webhook.secret}")
    String webhookSecret;
    public boolean isValid(byte[] rawBody, String signature){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(webhookSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));

            byte[] hmacBytes = mac.doFinal(rawBody);
            String computedSignature = toHex(hmacBytes);

            return computedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

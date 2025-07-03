package com.res_pvs.controller;



import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
@RequestMapping("/api/payment")
public class RazorpayWebhookHandler {

    @Value("${razorpay.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader("X-Razorpay-Signature") String signature) {
    	System.out.println("webhook");
    	System.out.println(payload);
    	System.out.println(signature);
    	

    	try {
            boolean verified = verifySignature(payload, signature, webhookSecret);
            if (verified) {
                System.out.println("✅ Payment Webhook Verified");
                System.out.println(payload); // parse and save data here
                return ResponseEntity.ok("Webhook handled");
            } else {
                System.out.println("❌ Webhook Signature Mismatch");
                return ResponseEntity.status(400).body("Invalid signature");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error");
        }
    }

    private boolean verifySignature(String payload, String actualSignature, String secret) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256Hmac.init(keySpec);
        byte[] hash = sha256Hmac.doFinal(payload.getBytes());
        
        String expectedSignature = new String(Hex.encodeHex(hash));
        return expectedSignature.equals(actualSignature);
    }
}


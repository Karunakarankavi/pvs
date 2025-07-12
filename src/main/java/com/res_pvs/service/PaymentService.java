package com.res_pvs.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class PaymentService {
	
	@Value("${razorpay.key_secret}")
    private String keySecret;
	
	
	public boolean verifySignature(String orderId, String paymentId, String razorpaySignature) {
	    try {
	        String payload = orderId + "|" + paymentId;
	        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	        SecretKeySpec secret_key = new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	        sha256_HMAC.init(secret_key);
	        byte[] hash = sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));
	        // Convert to HEX
	        String generatedSignature = toHex(hash);
	        return generatedSignature.equals(razorpaySignature);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	public static String toHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (byte b : hash) {
	        String hex = Integer.toHexString(0xff & b);
	        if (hex.length() == 1)
	            hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}




}

package com.res_pvs.controller;



import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;
    
    @PostMapping("/create-link")
    public ResponseEntity<Map<String, Object>> createPaymentLink(@RequestBody Map<String, Object> data) {
        try {
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

            // 1. Create customer first
            JSONObject customerRequest = new JSONObject();
            customerRequest.put("name", data.getOrDefault("name", "Test User"));
            customerRequest.put("email", data.getOrDefault("email", "test@example.com"));
            customerRequest.put("contact", data.getOrDefault("contact", "9876543210"));

            Customer customer = razorpay.customers.create(customerRequest);
            String customerId = customer.get("id");

            // 2. Notify options
            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);

            // 3. Create payment link
            JSONObject request = new JSONObject();
            request.put("amount", ((int) data.get("amount")) * 100); // convert to paise
            request.put("currency", "INR");
            request.put("description", "Payment for Order");
            request.put("customer_id", customerId); // ✅ key change here
            request.put("notify", notify);
            request.put("callback_url", "https://example.com/thank-you");
            request.put("callback_method", "get");

            PaymentLink link = razorpay.paymentLink.create(request);

            Map<String, Object> response = new HashMap<>();
            response.put("link", link.get("short_url"));
            response.put("id", link.get("id"));
            response.put("status", link.get("status"));

            return ResponseEntity.ok(response);

        } catch (RazorpayException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", e.getMessage()));
        }
    }

}


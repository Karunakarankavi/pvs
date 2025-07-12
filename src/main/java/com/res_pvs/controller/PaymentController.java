package com.res_pvs.controller;



import com.razorpay.*;
import com.res_pvs.dto.PaymentData;
import com.res_pvs.service.BookingService;
import com.res_pvs.service.PaymentService;
import com.res_pvs.service.RoomUserService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	
	private final RoomUserService roomUserService;
	private final PaymentService  paymentService;
	private final BookingService  bookingService;

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;
    
    String currency = "INR";
    
    public PaymentController(RoomUserService roomUserService ,PaymentService  paymentService , BookingService  bookingService ) {
		this.roomUserService = roomUserService ;
		this.paymentService  = paymentService;
		this.bookingService  =  bookingService; 
    	
    }
    
    @PostMapping("/createOrder")
    public String createOrder(@RequestParam int amount) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount*100);
        orderRequest.put("currency", currency);
        Order order = razorpay.orders.create(orderRequest);
		return order.toString();
    	
    }
    
    @PostMapping("/confirmAndBook")
    public String conformAndBook(@RequestBody PaymentData paymentData) {
    	String orderId = paymentData.getRazorpayOrderId();
    	String paymentId = paymentData.getRazorpayPaymentId();
    	String signature = paymentData.getRazorpaySignature();
    	if(paymentService.verifySignature(orderId, paymentId  ,signature )) {
    		bookingService.bookRoom(paymentData.getBookingRequest().getRoomId(), 
    				paymentData.getBookingRequest().getCheckIn(), 
    				paymentData.getBookingRequest().getCheckOut(), 
    				paymentData.getBookingRequest().getUserId());
    	}
		return "";
    	
    }

    
    @PostMapping("/create-link")
    public ResponseEntity<Map<String, Object>> createPaymentLink( @RequestParam String userId ,  @RequestBody Map<String, Object> data) throws Exception {
        try {
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

            // 1. Create customer first

            String customerId = roomUserService.createAndSaveRazorpayCustomer(userId);

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
            request.put("callback_url", "http://localhost:4200/");
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


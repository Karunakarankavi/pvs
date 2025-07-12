package com.res_pvs.service;

import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.razorpay.Customer;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.res_pvs.dto.ResponseMessage;
import com.res_pvs.entity.RoomUser;
import com.res_pvs.repo.UserRepo;

@Service
public class RoomUserService {
	
	private final UserRepo userrepo;
	private final ResponseMessage response;
    private final RazorpayClient razorpayClient;

	
	public RoomUserService(UserRepo userrepo, ResponseMessage response  , @Value("${razorpay.key_id}") String keyId,
            @Value("${razorpay.key_secret}") String keySecret) throws RazorpayException {
		this.userrepo = userrepo;
		this.response = response;
        this.razorpayClient = new RazorpayClient(keyId, keySecret);

		
	}
	
	public String createAndSaveRazorpayCustomer(String userId) throws Exception {
		RoomUser user = userrepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
       System.out.println(user.getRazorpayId());
        if (user.getRazorpayId() != null) {
            return user.getRazorpayId(); // Reuse existing
        }

        JSONObject customerRequest = new JSONObject();
        customerRequest.put("name", user.getName());
        customerRequest.put("email", user.getEmail());
        customerRequest.put("contact", user.getMobileNumber());
        Customer customer = razorpayClient.customers.create(customerRequest);
        String razorpayCustomerId = customer.get("id");

        // Save in DB
        user.setRazorpayId(razorpayCustomerId);
        userrepo.save(user);

        return razorpayCustomerId;
    }
	
	public ResponseEntity saveUser(RoomUser user) {
		try {
			this.userrepo.save(user);
			response.setMessage("User has been saved successfully");
			response.setStatus("success");
            return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("failure");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	public ResponseEntity getUser(String id) {
		try {
			Optional<RoomUser> user = this.userrepo.findById(id);
			response.setMessage("User information retrived successfully");
			response.setStatus("success");
			response.setInformations(user);
            return ResponseEntity.status(HttpStatus.OK).body(response);
		}catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("failure");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	public ResponseEntity updateUser(String id , RoomUser userDetails) {
		Optional<RoomUser> optionalUser = userrepo.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
        	RoomUser existingUser = optionalUser.get();
        	 // Update only non-null fields
            if (userDetails.getName() != null) {
                existingUser.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                existingUser.setEmail(userDetails.getEmail());
            }
            if (userDetails.getMobileNumber() != null) {
                existingUser.setMobileNumber(userDetails.getMobileNumber());
            }
            if (userDetails.getAddress() != null) {
                existingUser.setAddress(userDetails.getAddress());
            }
            if (userDetails.getZipcode() != 0) {
                existingUser.setZipcode(userDetails.getZipcode());
            }
            RoomUser updatedUser = userrepo.save(existingUser);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        	
        }catch(Exception e){
        	response.setMessage(e.getMessage());
        	response.setStatus("failure");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
	}

}

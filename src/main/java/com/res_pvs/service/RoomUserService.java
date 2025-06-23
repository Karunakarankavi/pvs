package com.res_pvs.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.res_pvs.dto.ResponseMessage;
import com.res_pvs.entity.RoomUser;
import com.res_pvs.repo.UserRepo;

@Service
public class RoomUserService {
	
	private final UserRepo userrepo;
	private final ResponseMessage response;
	
	public RoomUserService(UserRepo userrepo, ResponseMessage response ) {
		this.userrepo = userrepo;
		this.response = response;
		
	}
	
	public ResponseEntity saveUser(RoomUser user) {
		try {
			System.out.println(user.getEmail());
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
	
	public ResponseEntity updateUser(int id , RoomUser userDetails) {
		Optional<RoomUser> optionalUser = userrepo.findById(id);
		System.out.println(id);

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
            RoomUser updatedUser = userrepo.save(existingUser);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        	
        }catch(Exception e){
        	response.setMessage(e.getMessage());
        	response.setStatus("failure");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        	
        }
	}

}

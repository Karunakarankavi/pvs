package com.res_pvs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import com.res_pvs.entity.RoomUser;
import com.res_pvs.service.RoomUserService;

@RestController
public class UserController {
	
	private final RoomUserService userservice;
	
	public UserController(RoomUserService userservice) {
		this.userservice = userservice;
		
	}
	
	@PostMapping("/saveuser")
	public ResponseEntity saveUser(@RequestBody RoomUser user) {
		return userservice.saveUser(user);
	}
	
	
	@PutMapping("/updateuser")
	public ResponseEntity updateUser(@RequestParam int id , @RequestBody RoomUser user) {
		
		return userservice.updateUser(id, user);
	}
	
	
	

}

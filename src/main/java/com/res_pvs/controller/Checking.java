package com.res_pvs.controller;

import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Checking {
	
	@GetMapping("/check")
	public String check() {
		return "Working...zzzz";
	}
	
	

}

package com.res_pvs.dto;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessage {
	
	private Object informations;

	
	public Object getInformations() {
		return informations;
	}

	public void setInformations(Object informations) {
		this.informations = informations;
	}

	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String status;

}

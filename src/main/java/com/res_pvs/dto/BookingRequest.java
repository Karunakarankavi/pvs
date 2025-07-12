package com.res_pvs.dto;

import java.time.LocalDate;

public class BookingRequest {

    public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public LocalDate getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(LocalDate checkIn) {
		this.checkIn = checkIn;
	}
	public LocalDate getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(LocalDate checkOut) {
		this.checkOut = checkOut;
	}
	private String roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
     
    public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	private String userId;
    
    

}

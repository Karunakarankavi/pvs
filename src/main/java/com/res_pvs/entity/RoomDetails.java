package com.res_pvs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RoomDetails {
	
	@Id
    @Column(name = "room_id") // tells JPA to map this field to DB column `room_id`
    private String roomId;
	
	public String getRoom_id() {
		return roomId;
	}

	public void setRoom_id(String roomId) {
		this.roomId = roomId;
	}

	public int getBed_room() {
		return bed_room;
	}

	public void setBed_room(int bed_room) {
		this.bed_room = bed_room;
	}

	public int getRest_room() {
		return rest_room;
	}

	public void setRest_room(int rest_room) {
		this.rest_room = rest_room;
	}

	

	private int bed_room;
	
	private int rest_room;
	
    private Boolean air_conditioner;

	public Boolean getAir_conditioner() {
		return air_conditioner;
	}

	public void setAir_conditioner(Boolean air_conditioner) {
		this.air_conditioner = air_conditioner;
	}

}

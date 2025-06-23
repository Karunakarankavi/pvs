package com.res_pvs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.res_pvs.entity.RoomDetails;
import com.res_pvs.service.RoomService;

@RestController
public class RoomController {
	private final RoomService roomService;
	public RoomController(RoomService roomService) {
		this.roomService = roomService;
		
	}
	
	@PostMapping("/saveroom")
	public ResponseEntity saveRoom (@RequestBody RoomDetails room) {
		return roomService.saveRoom(room);
		
	}
	
	@GetMapping("/getAllRoom")
	public ResponseEntity getAllRoom() {
		return roomService.getAllRoom();
	}
	
	@GetMapping("/getRoomById")
	public ResponseEntity getRoomById(@RequestParam String id) {
		return roomService.getRoomById(id);
	}
	
	@PutMapping("/updateRoomById")
	public ResponseEntity updateRoomById(@RequestParam String id , @RequestBody RoomDetails room ) {
		return roomService.updateRoom(id, room);
	}
	
	@DeleteMapping("/deleteRoomById")
	public ResponseEntity deleteRoomById(@RequestParam String id) {
		return roomService.deleteRoomById(id);
	}
	
	

}

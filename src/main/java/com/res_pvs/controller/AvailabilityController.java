package com.res_pvs.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.res_pvs.service.RoomService;
import com.res_pvs.entity.RoomDetails;

@RestController
@RequestMapping("/rooms")
public class AvailabilityController {

    
    private final RoomService roomService;
    
    public AvailabilityController(RoomService roomService) {
		this.roomService = roomService;
    	
    }

    @GetMapping("/available")
    public ResponseEntity<List<RoomDetails>> getAvailableRooms(
        @RequestParam("checkIn") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
        @RequestParam("checkOut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    ) {
        List<RoomDetails> availableRooms = roomService.getAvailableRooms(checkIn, checkOut);
        return ResponseEntity.ok(availableRooms);
    }
}


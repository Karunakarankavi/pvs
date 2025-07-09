package com.res_pvs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.res_pvs.dto.BookingRequest;
import com.res_pvs.service.BookingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity<String> bookRoom( @RequestBody BookingRequest request) {
        String result = bookingService.bookRoom(
                request.getRoomId(),
                request.getCheckIn(),
                request.getCheckOut(),
                request.getUserId()
        );

        if (result.startsWith("Room is already booked")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }

        return ResponseEntity.ok(result);
    }
    
    @PostMapping("findAllBookingByUser")
    public ResponseEntity findAllBookingByUser(@RequestParam String id) {
    	return bookingService.findAllBookingByUser(id);
    }
    
    @PostMapping("findBookingById")
    public ResponseEntity findAllBookingById(@RequestParam int id) {
    	return bookingService.findBookingById(id);
    }
    
    @DeleteMapping("/cancelBooking")
    public ResponseEntity cancelBooking(@RequestParam long id) {
    	return bookingService.deleteBookingById(id);
    }
    
}


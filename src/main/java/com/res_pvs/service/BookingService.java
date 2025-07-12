package com.res_pvs.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.res_pvs.dto.ResponseMessage;
import com.res_pvs.entity.Booking;
import com.res_pvs.entity.RoomDetails;
import com.res_pvs.repo.BookingRepository;
import com.res_pvs.repo.RoomRepo;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final RoomRepo roomRepo ;
    
    private final ResponseMessage response;
    
    public BookingService(BookingRepository bookingRepository , RoomRepo roomRepo  ,  ResponseMessage response) {
		this.bookingRepository = bookingRepository;
		this.roomRepo = roomRepo;
		this.response = response;
    	
    }
    

    public String bookRoom(String roomId, LocalDate checkIn, LocalDate checkOut , String userId) {
        // Check if the room exists
        RoomDetails room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Check for conflicting bookings
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, checkIn, checkOut );
        System.out.println(conflicts);

        if (!conflicts.isEmpty()) {
            return "Room is already booked during the selected dates.";
        }

        // Save new booking
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setUserId(userId);

        bookingRepository.save(booking);

        return "Booking successful!";
    }
    
    public ResponseEntity findAllBookingByUser(String id) {
    	try {
    		List<Booking> allBooking = bookingRepository.findByUserId(id);
    		return ResponseEntity.status(HttpStatus.OK).body(allBooking);
    		
    		
    	}catch(Exception e) {
    		response.setMessage(e.getMessage());
    		response.setStatus("failure");
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    		
    	}
    }
    
    public ResponseEntity findBookingById(int id) {
    	try {
    		Optional<Booking> bookingDetails = bookingRepository.findById(id);
    		return ResponseEntity.status(HttpStatus.OK).body(bookingDetails);

    	}catch(Exception e) {
    		response.setMessage(e.getMessage());
    		response.setStatus("failure");
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    		
    	}
    }
    
    public ResponseEntity deleteBookingById(long id) {
    	try {
    		bookingRepository.deleteById(id);
    		response.setMessage("Booking cancelled successfully");
    		response.setStatus("success");
    		return ResponseEntity.status(HttpStatus.OK).body(response);
    		
    	}catch(Exception e) {
    		response.setMessage(e.getMessage());
    		response.setStatus("failure");
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    		
    	}
    }
}

package com.res_pvs.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.res_pvs.dto.ResponseMessage;
import com.res_pvs.entity.RoomDetails;
import com.res_pvs.repo.BookingRepository;
import com.res_pvs.repo.RoomRepo;

@Service
public class RoomService {
	
	private final RoomRepo roomrepo;
	private final ResponseMessage response;
	private final BookingRepository bookingRepository;
	
	public RoomService(RoomRepo roomrepo , ResponseMessage response , BookingRepository bookingRepository) {
		this.roomrepo = roomrepo;
		this.response = response;
		this.bookingRepository = bookingRepository;
		
	}
	
	
	public ResponseEntity saveRoom(RoomDetails room) {
		try {
			roomrepo.save(room);
			response.setMessage("room saved successfully");
			response.setStatus("success");
			return ResponseEntity.status(HttpStatus.OK).body(response);

			
		}catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("failure");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	public ResponseEntity getAllRoom() {
		try {
			Optional<List<RoomDetails>> roomDetails = Optional.ofNullable(roomrepo.findAll());
			return ResponseEntity.status(HttpStatus.OK).body(roomDetails);
		}catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("failure");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	public ResponseEntity getRoomById(String id) {
		try {
			Optional<RoomDetails> roomDetails = roomrepo.findById(id);
			return ResponseEntity.status(HttpStatus.OK).body(roomDetails);			
		}catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("failure");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			
		}
	}
	
	public ResponseEntity updateRoom(String id , RoomDetails updateRoomDetails) {
		try {
			Optional<RoomDetails> roomDetails = roomrepo.findById(id);
			RoomDetails existingRoom = roomDetails.get();
			if(updateRoomDetails.getBed_room() != 0) {
				existingRoom.setBed_room(updateRoomDetails.getBed_room());
			}
			if(updateRoomDetails.getRest_room() != 0) {
				existingRoom.setRest_room(updateRoomDetails.getRest_room());
			}
			if(updateRoomDetails.getAir_conditioner() != null) {
				existingRoom.setAir_conditioner(updateRoomDetails.getAir_conditioner());
			}
			roomrepo.save(existingRoom);
			return ResponseEntity.status(HttpStatus.OK).body(existingRoom);
 

			
		}catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("failure");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			
		}
	}
	
	public ResponseEntity deleteRoomById(String id) {
		try {
			if (roomrepo.existsById(id)) {
		        roomrepo.deleteById(id);
		        response.setMessage("deleted successfully");
		        response.setStatus("success");
				return ResponseEntity.status(HttpStatus.OK).body(response);

		    } else {
		    	response.setMessage("id not found");
		        response.setStatus("failure");
				return ResponseEntity.status(HttpStatus.OK).body(response);
		    }
			
		}catch(Exception e) {
			response.setMessage(e.getMessage());
			response.setStatus("failure");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			
		}
	}
	
	
	public List<RoomDetails> getAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        List<String> bookedRoomIds = bookingRepository.findBookedRoomIds(checkIn, checkOut);
        System.out.println(bookedRoomIds);
        if (bookedRoomIds.isEmpty()) {
            return roomrepo.findAll(); // All rooms are available
        }
        return roomrepo.findByRoomIdNotIn(bookedRoomIds);
    }
	
	

}

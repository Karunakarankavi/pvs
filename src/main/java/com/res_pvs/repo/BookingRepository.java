package com.res_pvs.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.res_pvs.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Query("SELECT b.room.id FROM Booking b WHERE " +
		       "(:checkIn <= b.checkOutDate AND :checkOut >= b.checkInDate)")
    List<String> findBookedRoomIds(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
    
	@Query("SELECT b FROM Booking b WHERE b.room.roomId = :roomId " +
		       "AND (:checkIn <= b.checkOutDate AND :checkOut >= b.checkInDate)")
		List<Booking> findConflictingBookings(@Param("roomId") String roomId,
		                                      @Param("checkIn") LocalDate checkIn,
		                                      @Param("checkOut") LocalDate checkOut);
	
	List<Booking> findByUserId(int id);
	
	Optional<Booking> findById(int id);

}


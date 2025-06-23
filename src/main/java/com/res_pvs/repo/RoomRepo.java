package com.res_pvs.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.res_pvs.entity.RoomDetails;

public interface RoomRepo extends JpaRepository<RoomDetails , String> {
	
	List<RoomDetails> findByRoomIdNotIn(List<String> roomIds);
	


}

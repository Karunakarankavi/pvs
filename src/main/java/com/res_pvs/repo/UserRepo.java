package com.res_pvs.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.res_pvs.entity.RoomUser;

public interface UserRepo extends JpaRepository<RoomUser , String> {


}

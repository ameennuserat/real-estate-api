package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomKey(String roomKey);
}

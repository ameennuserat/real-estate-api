package com.graduation.realestateconsulting.services;



import com.graduation.realestateconsulting.model.dto.request.RoomRequest;
import com.graduation.realestateconsulting.model.dto.response.RoomResponse;
import com.graduation.realestateconsulting.model.dto.response.UserRoomResponse;
import com.graduation.realestateconsulting.model.enums.RoomStatus;

import java.util.List;

public interface RoomService {
    
    List<UserRoomResponse> getAllRoomsByUserId(Long userId);
    RoomResponse getRoomById(Long id);
    RoomResponse createRoom(RoomRequest userRequest);
    RoomResponse updateRoomStatus(Long id, RoomStatus status);
    void deleteRoom(Long id);

}

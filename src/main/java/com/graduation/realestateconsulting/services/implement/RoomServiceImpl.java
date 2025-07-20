package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.RoomRequest;
import com.graduation.realestateconsulting.model.dto.response.RoomResponse;
import com.graduation.realestateconsulting.model.dto.response.UserRoomResponse;
import com.graduation.realestateconsulting.model.entity.Room;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.RoomStatus;
import com.graduation.realestateconsulting.model.mapper.RoomMapper;
import com.graduation.realestateconsulting.model.mapper.UserMapper;
import com.graduation.realestateconsulting.repository.RoomRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repo;
    private final UserRepository userRepo;
    private final RoomMapper mapper;
    private final UserMapper userMapper;

    @Override
    public List<UserRoomResponse> getAllRoomsByUserId(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User with id: " + userId + " not found"));

        List<UserRoomResponse> responses = new ArrayList<>();

        User u = null;
        for (Room r : user.getAllRoom()){
            if (r.getUser1().getId().equals(userId)){
                u = r.getUser2();
            }

            if (r.getUser2().getId().equals(userId)){
                u = r.getUser1();
            }

            responses.add(UserRoomResponse.builder()
                    .otherUser(userMapper.toDto(u))
                    .roomKey(r.getRoomKey())
                    .createdAt(r.getCreatedAt())
                    .build());

        }

        return responses;
    }

    @Override
    public RoomResponse getRoomById(Long id) {
        return mapper.toDto(repo.findById(id).orElseThrow(() -> new RuntimeException("Room with id: "+id+" not found")));
    }

    @Override
    public RoomResponse createRoom(RoomRequest userRequest) {
        Long id1 = userRequest.getUserId1();
        Long id2 = userRequest.getUserId2();

        if (id1 == null || id2 == null) {
            throw new IllegalStateException("Users must be set before saving the Room");
        }

        if (id1 > id2) {
            Long temp = id1;
            id1 = id2;
            id2 = temp;
        }
        String key = id1 + "_" + id2;

        if (repo.findByRoomKey(key).isPresent()) {
            throw new IllegalStateException("Room with key: "+key+" already exists");
        }

        Room room = Room.builder()
                .user1(User.builder().id(id1).build())
                .user2(User.builder().id(id2).build())
                .roomKey(key)
                .build();

        Room savedRoom = repo.save(room);

        return mapper.toDto(savedRoom);
    }

    @Override
    public RoomResponse updateRoomStatus(Long id, RoomStatus status) {
        Room room = repo.findById(id).orElseThrow(() -> new RuntimeException("Room with id: "+id+" not found"));
        room.setStatus(status);
        Room savedRoom = repo.save(room);
        return mapper.toDto(savedRoom);
    }

//    @Override
//    public RoomResponse updateRoom(Long id, RoomRequest roomRequest) {
//        Room room = repo.findById(id).orElseThrow(() -> new RuntimeException("Room with id: "+id+" not found"));
//        mapper.toEntity(room, roomRequest);
//        Room savedRoom = repo.save(room);
//        return mapper.toDto(savedRoom);
//    }

    @Override
    public void deleteRoom(Long id) {
        Room room = repo.findById(id).orElseThrow(() -> new RuntimeException("Room with id: "+id+" not found"));
        repo.delete(room);
    }
}

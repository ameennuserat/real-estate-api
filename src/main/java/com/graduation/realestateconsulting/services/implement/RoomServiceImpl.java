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
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repo;
    private final UserRepository userRepo;
    private final RoomMapper mapper;
    private final UserMapper userMapper;
    private final CacheManager cacheManager;

    @Cacheable(value = "userRooms", key = "#userId")
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
                    .id(r.getId())
                    .otherUser(userMapper.toDto(u))
                    .createdAt(r.getCreatedAt())
                    .build());

        }

        return responses;
    }

    @Cacheable(value = "rooms", key = "#id")
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

        Optional<Room> existRoom = repo.findByRoomKey(key);
        if (existRoom.isPresent()) {
//            throw new IllegalArgumentException("Room with key: "+key+" already exists");
            return mapper.toDto(existRoom.get());
        }

        User user1 = userRepo.findById(id1).orElseThrow(() -> new IllegalArgumentException("user1 not found"));
        User user2 = userRepo.findById(id2).orElseThrow(() -> new IllegalArgumentException("user2 not found"));

        Room room = Room.builder()
                .user1(user1)
                .user2(user2)
                .roomKey(key)
                .status(RoomStatus.UNLOCKED)
                .build();

        Room savedRoom = repo.save(room);
        Cache userRoomsCache = cacheManager.getCache("userRooms");
        if (userRoomsCache != null) {
            userRoomsCache.evict(user1.getId());
            userRoomsCache.evict(user2.getId());
            log.info("Evicted userRooms cache for users {} and {}", user1.getId(), user2.getId());
        }
        return mapper.toDto(savedRoom);
    }

    @CachePut(value = "rooms" , key = "#id")
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

        Cache roomsCache = cacheManager.getCache("rooms");
        Cache userRoomsCache = cacheManager.getCache("userRooms");
        if (roomsCache != null && userRoomsCache != null) {
            roomsCache.evict(id);
            userRoomsCache.evict(room.getUser1().getId());
            userRoomsCache.evict(room.getUser2().getId());
            log.info("Evicted all caches related to room {}", id);
        }

        repo.delete(room);
    }
}

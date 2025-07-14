package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.MessageRequest;
import com.graduation.realestateconsulting.model.dto.response.MessageResponse;
import com.graduation.realestateconsulting.model.entity.Message;
import com.graduation.realestateconsulting.model.entity.Room;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.mapper.MessageMapper;
import com.graduation.realestateconsulting.repository.MessageRepository;
import com.graduation.realestateconsulting.repository.RoomRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository repo;
    private final RoomRepository roomRepo;
    private final UserRepository userRepo;
    private final MessageMapper mapper;


    @Override
    public List<MessageResponse> getAllMessagesByRoomId(Long roomId, Pageable pageable) {
        return repo.findByRoomIdOrderByCreatedAtDesc(roomId,pageable).map(mapper::toDto).stream().toList();
    }

    @Override
    public void deleteMessage(Long id) {
        Message room = repo.findById(id).orElseThrow(() -> new RuntimeException("Message with id: " + id + " not found"));
        repo.delete(room);
    }

    @Override
    public MessageResponse createMessage(MessageRequest request) {
        Room room = roomRepo.findById(request.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        User sender = userRepo.findById(request.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Message message = Message.builder()
                .room(room)
                .content(request.getContent())
                .sender(sender)
                .createdAt(LocalDateTime.now())
                .build();
        Message savedMessage = repo.save(message);
        return mapper.toDto(savedMessage);
    }
}

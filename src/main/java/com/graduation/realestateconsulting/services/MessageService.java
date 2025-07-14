package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.MessageRequest;
import com.graduation.realestateconsulting.model.dto.response.MessageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    
    List<MessageResponse> getAllMessagesByRoomId(Long roomId, Pageable pageable);
    MessageResponse createMessage(MessageRequest request);
    void deleteMessage(Long id);

}

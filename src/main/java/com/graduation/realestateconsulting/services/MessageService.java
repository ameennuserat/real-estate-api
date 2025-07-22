package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.MessageRequest;
import com.graduation.realestateconsulting.model.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    
    Page<MessageResponse> getAllMessagesByRoomId(Long roomId, Pageable pageable);
    MessageResponse createMessage(MessageRequest request);
    void deleteMessage(Long id);

}

package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.request.ChatFileRequest;
import com.graduation.realestateconsulting.model.dto.request.ChatMessageRequest;
import com.graduation.realestateconsulting.model.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    
    Page<MessageResponse> getAllMessagesByRoomId(Long roomId, Pageable pageable);
    MessageResponse createMessage(ChatMessageRequest request);
    MessageResponse createFile(String path,ChatFileRequest request);
    void deleteMessage(Long id);

}

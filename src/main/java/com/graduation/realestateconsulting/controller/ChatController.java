package com.graduation.realestateconsulting.controller;


import com.graduation.realestateconsulting.model.dto.request.ChatFileRequest;
import com.graduation.realestateconsulting.model.dto.request.ChatMessageRequest;
import com.graduation.realestateconsulting.model.dto.response.MessageResponse;
import com.graduation.realestateconsulting.services.ImageService;
import com.graduation.realestateconsulting.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final ImageService imageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, MessageService messageService, ImageService imageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
        this.imageService = imageService;
    }

    @MessageMapping("/chat/send-message")
    public void sendMessage(@Payload ChatMessageRequest request) {
        MessageResponse response = messageService.createMessage(request);
        System.out.println("/topic/room/"+request.getRoomId());
        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId(), response);
    }

    @MessageMapping("/chat/send-file")
    public void sendFile(@Payload ChatFileRequest request) throws IOException {

        String path = imageService.uploadBinary(request.getFileName(),request.getFilaData());

        MessageResponse response = messageService.createFile(path,request);
        System.out.println("/topic/room/"+request.getRoomId());
        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId(), response);
    }

}

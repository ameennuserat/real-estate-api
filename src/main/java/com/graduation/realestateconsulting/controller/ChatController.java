package com.graduation.realestateconsulting.controller;


import com.graduation.realestateconsulting.model.dto.request.MessageRequest;
import com.graduation.realestateconsulting.model.dto.response.MessageResponse;
import com.graduation.realestateconsulting.services.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    public ChatController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/chat/send-message")
    public void sendMessage(@Payload MessageRequest request) {
        MessageResponse response = messageService.createMessage(request);
        System.out.println("/topic/room/"+request.getRoomId());
        messagingTemplate.convertAndSend("/topic/room/" + request.getRoomId(), response);
    }

}

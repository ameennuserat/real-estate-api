package com.graduation.realestateconsulting.controller;


import com.graduation.realestateconsulting.model.dto.request.RoomRequest;
import com.graduation.realestateconsulting.model.enums.RoomStatus;
import com.graduation.realestateconsulting.services.MessageService;
import com.graduation.realestateconsulting.services.RoomService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomService service;
    private final MessageService messageService;

    public RoomController(RoomService service, MessageService messageService) {
        this.service = service;
        this.messageService = messageService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable long id) {
        return ResponseEntity.ok(service.getRoomById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getRoomsByUserId(@PathVariable long id) {
        return ResponseEntity.ok(service.getAllRoomsByUserId(id));
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.createRoom(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestParam(value = "status") RoomStatus status) {
        return ResponseEntity.ok(service.updateRoomStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable long id) {
        service.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId,
                                             @RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                             @RequestParam(value = "size",defaultValue = "20",required = false) int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return  ResponseEntity.ok(messageService.getAllMessagesByRoomId(roomId,pageRequest));
    }

}

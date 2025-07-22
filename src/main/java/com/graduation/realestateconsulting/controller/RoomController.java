package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.RoomRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.enums.RoomStatus;
import com.graduation.realestateconsulting.services.MessageService;
import com.graduation.realestateconsulting.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService service;
    private final MessageService messageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomById(@PathVariable long id) {
        GlobalResponse response = GlobalResponse.builder().status("success")
                .data(service.getRoomById(id)).build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getRoomsByUserId(@PathVariable long id) {
        GlobalResponse response = GlobalResponse.builder().status("success")
                .data(service.getAllRoomsByUserId(id)).build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success")
                .data(service.createRoom(request)).build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestParam(value = "status") RoomStatus status) {
        GlobalResponse response = GlobalResponse.builder().status("success")
                .data(service.updateRoomStatus(id, status)).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable long id) {
        service.deleteRoom(id);
        GlobalResponse response = GlobalResponse.builder().status("success")
                .data("message => Room with id: "+id+" deleted successfully").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId,
                                             @RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                             @RequestParam(value = "size",defaultValue = "20",required = false) int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        GlobalResponse response = GlobalResponse.builder().status("success")
                .data(messageService.getAllMessagesByRoomId(roomId,pageRequest)).build();
        return  ResponseEntity.ok(response);
    }

}

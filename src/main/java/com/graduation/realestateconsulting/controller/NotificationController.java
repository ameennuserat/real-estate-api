package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(
            summary = "Get my notifications",
            description = "Returns a paginated list of notifications for the currently logged-in user."
    )
    @GetMapping("/my-notifications")
    public ResponseEntity<?> getMyNotifications(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(notificationService.getNotifications(currentUser, pageable))
                .build();

        return ResponseEntity.ok(globalResponse);
    }
}
package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.model.dto.request.CreateCouponRequest;
import com.graduation.realestateconsulting.model.dto.request.UpdateCouponRequest;
import com.graduation.realestateconsulting.model.dto.response.ExceptionResponse;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.services.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/coupons")
@Tag(name = "Coupons", description = "Operations related to coupon management")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @Operation(
            summary = "Create a new coupon",
            description = "Allows EXPERT or ADMIN users to create a new coupon.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Coupon created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400 or 500", description = "Validation or server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PostMapping()
    @PreAuthorize("hasAuthority('EXPERT') or hasAuthority('ADMIN')")
    public ResponseEntity<?> createCoupon(@AuthenticationPrincipal User currentUser, @Valid @RequestBody CreateCouponRequest request) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(couponService.createCoupon(request, currentUser))
                .build();
        return new ResponseEntity<>(globalResponse, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update a coupon",
            description = "Allows authorized users to update coupon data based on coupon ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coupon updated successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400 or 500", description = "Validation or server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @PatchMapping("/{couponId}")
    public ResponseEntity<?> updateCoupon(@PathVariable Long couponId,
                                          @AuthenticationPrincipal User currentUser,
                                          @RequestBody UpdateCouponRequest request) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(couponService.updateCoupon(couponId, request, currentUser))
                .build();
        return new ResponseEntity<>(globalResponse, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all coupons",
            description = "Returns a list of all coupons in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of coupons returned",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class)))
            }
    )
   // @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllCoupons() {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(couponService.getAllCoupons())
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @Operation(
            summary = "Get coupons by expert ID",
            description = "Returns a list of coupons created by a specific expert.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coupons retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400 or 500", description = "Invalid expert ID or server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/all/{expertId}")
    public ResponseEntity<?> getCouponByExpertId(@PathVariable Long expertId) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(couponService.getAllCouponsByExpertId(expertId))
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @Operation(
            summary = "Get coupon by ID",
            description = "Returns a single coupon by its unique ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coupon retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400 or 404", description = "Coupon not found or invalid ID",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable Long id) {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(couponService.getCouponById(id))
                .build();
        return ResponseEntity.ok(globalResponse);
    }

    @Operation(
            summary = "Get all coupons created by admin ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Coupon retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GlobalResponse.class))),
                    @ApiResponse(responseCode = "400 or 404", description = "Coupon not found or invalid ID",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            }
    )
    @GetMapping("/admin-coupons")
    public ResponseEntity<?> getGeneralCoupons() {
        GlobalResponse globalResponse = GlobalResponse.builder()
                .status("Success")
                .data(couponService.getGeneralCoupons())
                .build();
        return ResponseEntity.ok(globalResponse);
    }
}

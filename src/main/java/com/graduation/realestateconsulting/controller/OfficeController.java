package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.filter.dto.FilterRequestDto;
import com.graduation.realestateconsulting.filter.dto.PageRequestDto;
import com.graduation.realestateconsulting.filter.service.FiltersSpecificationService;
import com.graduation.realestateconsulting.model.dto.request.OfficeImageRequest;
import com.graduation.realestateconsulting.model.dto.request.OfficeRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.entity.Office;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.services.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService service;

    private final FiltersSpecificationService<Office> officeFilter;

    @PostMapping("/filter")
    public  ResponseEntity<?> filterOffice(@RequestBody FilterRequestDto request) {
        Specification<Office> officeSpecification =  officeFilter.getSearchSpecification(request.getFilterItems(),request.getGlobalOperator());
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequest());
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.filterOffice(officeSpecification,pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "page",defaultValue = "0") int page,
                                     @RequestParam(value = "size",defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.findAll(pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<?> findAllByUserStatus(@RequestParam(name = "status") UserStatus status) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.findAllByUserStatus(status))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.findById(id))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.getMe())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updateMe(@RequestBody OfficeRequest request) {
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.updateMe(request))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "upload-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@ModelAttribute OfficeImageRequest request) throws IOException {
        service.uploadImage(request);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(ResponseEntity.noContent().build().getStatusCode())
                .build();
        return ResponseEntity.ok(response);
    }

}
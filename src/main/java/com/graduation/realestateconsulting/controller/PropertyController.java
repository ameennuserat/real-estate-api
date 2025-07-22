package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.filter.dto.FilterRequestDto;
import com.graduation.realestateconsulting.filter.dto.PageRequestDto;
import com.graduation.realestateconsulting.filter.service.FiltersSpecificationService;
import com.graduation.realestateconsulting.model.dto.request.PropertyRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.entity.Property;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import com.graduation.realestateconsulting.services.PropertyService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService service;

    private final FiltersSpecificationService<Property> propertyFilter;

    @PostMapping("/filter")
    public  ResponseEntity<?> filterProperty(@RequestBody FilterRequestDto request) {
        Specification<Property> propertySpecification =  propertyFilter.getSearchSpecification(request.getFilterItems(),request.getGlobalOperator());
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequest());
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.filterProperty(propertySpecification,pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findAll(pageable)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filters")
    public ResponseEntity<?> findByFilters(@RequestParam(required = false) String name,
                                           @RequestParam(required = false) Double lowPrice,
                                           @RequestParam(required = false) Double highPrice,
                                           @RequestParam(required = false) ServiceType serviceType,
                                           @RequestParam(required = false) HouseType houseType,
                                           @RequestParam(required = false) String lowArea,
                                           @RequestParam(required = false) String highArea,
                                           @RequestParam(required = false) String location) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findByFilters(name, lowPrice, highPrice, serviceType, houseType, lowArea, highArea, location)).build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/office/{officeId}")
    public ResponseEntity<?> findAllByOfficeId(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size,
                                               @PathVariable Long officeId) {
        Pageable pageable = PageRequest.of(page, size);
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findAllByOfficeId(pageable, officeId)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@Parameter(description = "ID of the property to retrieve", required = true, example = "1") @PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findById(id)).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody PropertyRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.save(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody PropertyRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.update(id, request)).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) throws IOException {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder().status("success").data("Message => Property deleted successfully").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
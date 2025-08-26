package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.filter.dto.FilterRequestDto;
import com.graduation.realestateconsulting.filter.dto.PageRequestDto;
import com.graduation.realestateconsulting.filter.service.FiltersSpecificationService;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    private final FiltersSpecificationService<Client> clientFilter;

    @PostMapping("/filter")
    public  ResponseEntity<?> filterClient(@RequestBody FilterRequestDto request) {
        Specification<Client> clientSpecification =  clientFilter.getSearchSpecification(request.getFilterItems(),request.getGlobalOperator());
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequest());
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.filterClient(clientSpecification,pageable))
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

    @PostMapping("/add-follower/{expertId}")
    public ResponseEntity<?> addFollow(@PathVariable Long expertId) {
        service.addFollower(expertId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Follow created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/add-favorite/{expertId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long expertId) {
        service.addFavorite(expertId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Favorite created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rate-expert/{expertId}")
    public ResponseEntity<?> rateExpert(@PathVariable Long expertId, @RequestParam double rate) {
        service.rateExpert(expertId, rate);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Expert rated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/rate-office/{officeId}")
    public ResponseEntity<?> rateOffice(@PathVariable Long officeId, @RequestParam double rate) {
        service.rateOffice(officeId, rate);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Office rated successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/remove-follower/{expertId}")
    public ResponseEntity<?> removeFollow(@PathVariable Long expertId) {
        service.deleteFollower(expertId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Follow removed successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @DeleteMapping("/remove-favorite/{expertId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long expertId) {
        service.deleteFavorite(expertId);
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data("Favorite removed successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}
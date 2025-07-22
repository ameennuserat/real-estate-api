package com.graduation.realestateconsulting.controller;

import com.graduation.realestateconsulting.filter.dto.FilterRequestDto;
import com.graduation.realestateconsulting.filter.dto.PageRequestDto;
import com.graduation.realestateconsulting.filter.service.FiltersSpecificationService;
import com.graduation.realestateconsulting.model.dto.request.TicketRequest;
import com.graduation.realestateconsulting.model.dto.response.GlobalResponse;
import com.graduation.realestateconsulting.model.entity.Ticket;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import com.graduation.realestateconsulting.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;

    private final FiltersSpecificationService<Ticket> ticketFilter;

    @PostMapping("/filter")
    public  ResponseEntity<?> filterTicket(@RequestBody FilterRequestDto request) {
        Specification<Ticket> ticketSpecification =  ticketFilter.getSearchSpecification(request.getFilterItems(),request.getGlobalOperator());
        Pageable pageable = new PageRequestDto().getPageable(request.getPageRequest());
        GlobalResponse response = GlobalResponse.builder()
                .status("Success")
                .data(service.filterTicket(ticketSpecification,pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
  public ResponseEntity<?> findAll(@RequestParam(value = "page",defaultValue = "0") int page,
                                   @RequestParam(value = "size",defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.findAll(pageable))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filters")
    public ResponseEntity<?> findByFilters(@RequestParam(required = false) Double lowPrice,
                                           @RequestParam(required = false) Double highPrice,
                                           @RequestParam(required = false) ServiceType serviceType,
                                           @RequestParam(required = false) HouseType houseType,
                                           @RequestParam(required = false) String lowArea,
                                           @RequestParam(required = false) String highArea,
                                           @RequestParam(required = false) String location) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findByFilters(lowPrice, highPrice, serviceType, houseType, lowArea, highArea, location)).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> findAllByClientId(@RequestParam(value = "page",defaultValue = "0") int page,
                                               @RequestParam(value = "size",defaultValue = "10") int size,
                                               @PathVariable Long clientId) {
        Pageable pageable = PageRequest.of(page, size);
        GlobalResponse response = GlobalResponse.builder()
                .status("success")
                .data(service.findAllByClientId(pageable, clientId))
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.findById(id)).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody TicketRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.save(request)).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TicketRequest request) {
        GlobalResponse response = GlobalResponse.builder().status("success").data(service.update(id, request)).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        service.delete(id);
        GlobalResponse response = GlobalResponse.builder().status("success").data("Message => Ticket deleted successfully").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
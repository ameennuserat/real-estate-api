package com.graduation.realestateconsulting.services;


import com.graduation.realestateconsulting.model.dto.request.TicketRequest;
import com.graduation.realestateconsulting.model.dto.response.TicketResponse;
import com.graduation.realestateconsulting.model.entity.Ticket;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TicketService {

    Page<TicketResponse> findAll(Pageable pageable);
    List<TicketResponse> findByFilters(Double lowPrice, Double highPrice, ServiceType serviceType, HouseType houseType, String lowArea, String highArea, String location);
    Page<TicketResponse> findAllByClientId(Pageable pageable, Long officeId);

    TicketResponse findById(Long id);
    TicketResponse save(TicketRequest request);
    TicketResponse update(Long id,TicketRequest request);
    void delete(Long id);


    Page<TicketResponse> filterTicket(Specification<Ticket> ticketSpecification, Pageable pageable);
}
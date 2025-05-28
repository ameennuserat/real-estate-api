package com.graduation.realestateconsulting.services;


import com.graduation.realestateconsulting.model.dto.request.TicketRequest;
import com.graduation.realestateconsulting.model.dto.response.TicketResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TicketService {

    Page<TicketResponse> findAll(Pageable pageable);
    Page<TicketResponse> findAllByOfficeId(Pageable pageable, Long officeId);
    TicketResponse findById(Long id);
    TicketResponse save(TicketRequest request);
    TicketResponse update(Long id,TicketRequest request);
    void delete(Long id);


}
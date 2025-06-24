package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.TicketRequest;
import com.graduation.realestateconsulting.model.dto.response.TicketResponse;
import com.graduation.realestateconsulting.model.entity.Ticket;
import com.graduation.realestateconsulting.model.enums.HouseType;
import com.graduation.realestateconsulting.model.enums.ServiceType;
import com.graduation.realestateconsulting.model.mapper.TicketMapper;
import com.graduation.realestateconsulting.repository.TicketRepository;
import com.graduation.realestateconsulting.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository repository;
    private final TicketMapper mapper;

    @Override
    public Page<TicketResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public List<TicketResponse> findByFilters(Double lowPrice, Double highPrice, ServiceType serviceType, HouseType houseType, String lowArea, String highArea, String location) {
        return mapper.toDtos(repository.findByFilters(lowPrice, highPrice, serviceType, houseType, lowArea, highArea, location));
    }

    @Override
    public Page<TicketResponse> findAllByClientId(Pageable pageable, Long officeId) {
        return repository.findAllByClientId(pageable, officeId).map(mapper::toDto);
    }

    @Override
    public TicketResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
    }

    @Override
    public TicketResponse save(TicketRequest request) {

        Ticket ticket = mapper.toEntity(request);
        Ticket saved = repository.save(ticket);
        return mapper.toDto(saved);
    }

    @Override
    public TicketResponse update(Long id, TicketRequest request) {
        Ticket ticket = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        mapper.toEntity(ticket, request);
        Ticket saved = repository.save(ticket);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
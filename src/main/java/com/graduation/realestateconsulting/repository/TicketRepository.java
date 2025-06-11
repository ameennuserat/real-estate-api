package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findAllByClientId(Pageable pageable, Long clientId);
}
package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
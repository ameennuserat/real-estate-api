package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.ChatRequest;

public interface ChatRequestRepository extends JpaRepository<ChatRequest, Long> {

}
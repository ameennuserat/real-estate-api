package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
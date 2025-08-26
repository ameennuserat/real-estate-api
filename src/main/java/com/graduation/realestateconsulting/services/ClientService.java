package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.response.ClientResponse;
import com.graduation.realestateconsulting.model.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ClientService {

    Page<ClientResponse> findAll(Pageable pageable);

    ClientResponse findById(Long id);

    ClientResponse getMe();

    void addFollower(Long id);

    void addFavorite(Long id);

    void deleteFollower(Long id);

    void deleteFavorite(Long id);

    void rateExpert(Long id, double rate);

    void rateOffice(Long id, double rate);

    Page<ClientResponse> filterClient(Specification<Client> clientSpecification, Pageable pageable);
}
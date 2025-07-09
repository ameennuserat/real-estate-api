package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.response.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    Page<ClientResponse> findAll(Pageable pageable);

    ClientResponse findById(Long id);

    ClientResponse getMe();

    void addFollower(Long id);

    void addFavorite(Long id);

    void deleteFollower(Long id);

    void deleteFavorite(Long id);
}
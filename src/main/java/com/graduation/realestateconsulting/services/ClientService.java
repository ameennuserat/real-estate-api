package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.dto.response.ClientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {

    Page<ClientResponse> findAll(Pageable pageable);

    ClientResponse findById(Long id);

    ClientResponse getMe();

    void addFollow(Long id);
    void addFavorite(Long id);

    void deleteFollow(Long id);
    void deleteFavorite(Long id);
}
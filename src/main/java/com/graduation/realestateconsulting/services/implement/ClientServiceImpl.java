package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.response.ClientResponse;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.mapper.ClientMapper;
import com.graduation.realestateconsulting.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.ClientService;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService{

    private final ClientRepository repository;
    private final ClientMapper mapper;

    @Override
    public Page<ClientResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public ClientResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

    @Override
    public ClientResponse getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return repository.findByUserId(user.getId()).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

    @Override
    public void addFollow(Long id) {
        Client client = getCurrentClient();
        String followIds = client.getFollowing();

        if(followIds == null ||followIds.isEmpty()) {
            followIds = id.toString();
        }else{
            followIds += ","+id;
        }
        client.setFollowing(followIds);
        repository.save(client);
    }

    @Override
    public void addFavorite(Long id) {
        Client client = getCurrentClient();
        String favoriteIds = client.getFavorites();

        if(favoriteIds == null || favoriteIds.isEmpty()) {
            favoriteIds = id.toString();
        }else{
            favoriteIds += ","+id;
        }
        client.setFavorites(favoriteIds);
        repository.save(client);

    }

    @Override
    public void deleteFollow(Long id) {
        Client client = getCurrentClient();
        String followIds = client.getFollowing();

        // remove from end
        followIds = followIds.replaceAll(","+id, "");

        // remove from start
        followIds = followIds.replaceAll(id+",", "");

        client.setFollowing(followIds);
        repository.save(client);
    }

    @Override
    public void deleteFavorite(Long id) {
        Client client = getCurrentClient();
        String favoriteIds = client.getFavorites();

        // remove from end
        favoriteIds = favoriteIds.replaceAll(","+id, "");

        // remove from start
        favoriteIds = favoriteIds.replaceAll(id+",", "");

        client.setFavorites(favoriteIds);
        repository.save(client);
    }

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Client getCurrentClient(){
        User user = getCurrentUser();
        return repository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

}
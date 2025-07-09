package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.response.ClientResponse;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.mapper.ClientMapper;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.ClientService;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ExpertRepository expertRepository;
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
    public void addFollower(Long id) {
        Expert expert = expertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expert not found"));

        Client client = getCurrentClient();
        String followIds = client.getFollowers();

        if (isIdFoundInList(followIds, id)) {
            throw new IllegalArgumentException("You are already followed this expert");
        }

        String updatedFollowIds = followIds == null || followIds.isEmpty() ? id.toString() : followIds + "," + id;

        client.setFollowers(updatedFollowIds);
        repository.save(client);

        updateExpertFollowerCount(expert, 1);
    }

    @Override
    public void addFavorite(Long id) {
        Expert expert = expertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expert not found"));

        Client client = getCurrentClient();
        String favoriteIds = client.getFavorites();

        if (isIdFoundInList(favoriteIds, id)) {
            throw new IllegalArgumentException("You are already favorite this expert");
        }

        String updatedFavoriteIds = favoriteIds == null || favoriteIds.isEmpty() ? id.toString() : favoriteIds + "," + id;

        client.setFavorites(updatedFavoriteIds);
        repository.save(client);

        updateExpertFavoriteCount(expert, 1);
    }

    @Override
    public void deleteFollower(Long id) {
        Expert expert = expertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expert not found"));

        Client client = getCurrentClient();
        String followIds = client.getFollowers();

        if (!isIdFoundInList(followIds, id)) {
            throw new IllegalArgumentException("You are not followed this expert");
        }

        String updatedFollowerIds = removeIdFromList(followIds, id);

        client.setFollowers(updatedFollowerIds);
        repository.save(client);

        updateExpertFollowerCount(expert, -1);
    }

    @Override
    public void deleteFavorite(Long id) {
        Expert expert = expertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expert not found"));

        Client client = getCurrentClient();
        String favoriteIds = client.getFavorites();

        if (!isIdFoundInList(favoriteIds, id)) {
            throw new IllegalArgumentException("You are not favorite this expert");
        }

        String updatedFavoriteIds = removeIdFromList(favoriteIds, id);

        client.setFavorites(updatedFavoriteIds);
        repository.save(client);

        updateExpertFavoriteCount(expert, -1);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Client getCurrentClient() {
        User user = getCurrentUser();
        return repository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

    private boolean isIdFoundInList(String list, Long idToCheck) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        return Arrays.asList(list.split(",")).contains(idToCheck.toString());
    }

    private String removeIdFromList(String list, Long idToRemove) {
        return Arrays.stream(list.split(",")).filter(id -> !id.equals(idToRemove.toString())).collect(Collectors.joining(","));
    }

    private void updateExpertFollowerCount(Expert expert, Integer value) {
        Integer count = expert.getFollowersCount() == null ? 0 : expert.getFollowersCount();
        expert.setFollowersCount(count + value);
        expertRepository.save(expert);
    }

    private void updateExpertFavoriteCount(Expert expert, Integer value) {
        Integer count = expert.getFavoritesCount() == null ? 0 : expert.getFavoritesCount();
        expert.setFavoritesCount(count + value);
        expertRepository.save(expert);
    }
}
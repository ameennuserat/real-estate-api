package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.response.ClientResponse;
import com.graduation.realestateconsulting.model.entity.*;
import com.graduation.realestateconsulting.model.mapper.ClientMapper;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.OfficeRepository;
import com.graduation.realestateconsulting.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.graduation.realestateconsulting.services.ClientService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ClientRepository repository;
    private final ExpertRepository expertRepository;
    private final OfficeRepository officeRepository;
    private final RateRepository rateRepository;
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

    @Override
    public void rateExpert(Long id, double rate) {
        Expert expert = expertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
        Client client = getCurrentClient();

        List<Rate> rates = rateRepository.findByClientId(client.getId());
        Rate getRate = rates.stream().filter(r -> r.getExpert() == expert).findFirst().orElse(null);
        if (getRate == null) {
            getRate = Rate.builder()
                    .rate(rate)
                    .client(client)
                    .expert(expert)
                    .build();
            updateExpertRate(expert, rate, 0, true);
        } else {
            double oldRate = getRate.getRate();
            getRate.setRate(rate);
            updateExpertRate(expert, rate, oldRate, false);
        }
        rateRepository.save(getRate);
    }

    @Override
    public void rateOffice(Long id, double rate) {
        Office office = officeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Office not found"));
        Client client = getCurrentClient();

        List<Rate> rates = rateRepository.findByClientId(client.getId());
        Rate getRate = rates.stream().filter(r -> r.getOffice() == office).findFirst().orElse(null);
        if (getRate == null) {
            getRate = Rate.builder()
                    .rate(rate)
                    .client(client)
                    .office(office)
                    .build();
            updateOfficeRate(office, rate, 0, true);
        } else {
            double oldRate = getRate.getRate();
            getRate.setRate(rate);
            updateOfficeRate(office, rate, oldRate, false);
        }
        rateRepository.save(getRate);
    }

    @Override
    public Page<ClientResponse> filterClient(Specification<Client> clientSpecification, Pageable pageable) {
        return repository.findAll(clientSpecification, pageable).map(mapper::toDto);
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

    private void updateExpertRate(Expert expert, double newRate, double oldRate, boolean forAdd) {
        double count = expert.getRateCount();
        double totalRate = expert.getTotalRate();

        if (forAdd) {
            expert.setRateCount(count + 1);
            expert.setTotalRate(totalRate + newRate);
        } else {
            expert.setTotalRate(totalRate - oldRate + newRate);
        }

        expertRepository.save(expert);
    }

    private void updateOfficeRate(Office office, double newRate, double oldRate, boolean forAdd) {
        double count = office.getRateCount();
        double totalRate = office.getTotalRate();

        if (forAdd) {
            office.setRateCount(count + 1);
            office.setTotalRate(totalRate + newRate);
        } else {
            office.setTotalRate(totalRate - oldRate + newRate);
        }

        System.out.println("updateOfficeRate");
        officeRepository.save(office);
    }
}
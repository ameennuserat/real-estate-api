package com.graduation.realestateconsulting.observer.listiners;

import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.observer.events.CreateClientEvent;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateClientListener {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @EventListener
    public void createClientEvent(CreateClientEvent event) {
//        event.getUser().setEnable(true);
        event.getUser().setStatus(UserStatus.AVAILABLE);
        userRepository.save(event.getUser());
        Client client = Client.builder().user(event.getUser()).build();
        clientRepository.save(client);
    }

}
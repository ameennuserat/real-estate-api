package com.graduation.realestateconsulting.config;

import com.graduation.realestateconsulting.model.dto.request.LoginRequest;
import com.graduation.realestateconsulting.model.dto.response.LoginResponse;
import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.Office;
import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.OfficeRepository;
import com.graduation.realestateconsulting.repository.UserRepository;
import com.graduation.realestateconsulting.services.AuthService;
import com.graduation.realestateconsulting.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ExpertRepository expertRepository;
    private final OfficeRepository officeRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .enable(true)
                    .role(Role.ADMIN)
                    .status(UserStatus.AVAILABLE)
                    .phone("0999999999")
                    .build());

            User user =  userRepository.save(User.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .enable(true)
                    .role(Role.USER)
                    .status(UserStatus.AVAILABLE)
                    .phone("0999999999")
                    .build());

            clientRepository.save(
                    Client.builder()
                            .user(user)
                            .build()
            );

            User expert = userRepository.save(User.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .email("expert@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .enable(true)
                    .role(Role.EXPERT)
                    .status(UserStatus.AVAILABLE)
                    .phone("0999999999")
                    .build());
            expertRepository.save(
                    Expert.builder()
                            .user(expert)
                            .rateCount(1)
                            .experience("expert")
                            .profession("expert")
                            .idCardImage("expert")
                            .degreeCertificateImage("expert")
                            .totalRate(5)
                            .build()
            );

            User office = userRepository.save(User.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .email("office@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .enable(true)
                    .role(Role.OFFICE)
                    .status(UserStatus.AVAILABLE)
                    .phone("0999999999")
                    .build());

            officeRepository.save(Office.builder()
                            .user(office)
                            .location("office")
                            .latitude(22)
                            .longitude(32)
                            .commercialRegisterImage("office")
                    .build());
        }


        LoginResponse admin = authService.login(LoginRequest.builder()
                .email("admin@gmail.com")
                .password("12345678")
                .build());
        LoginResponse user = authService.login(LoginRequest.builder()
                .email("user@gmail.com")
                .password("12345678")
                .build());
        LoginResponse expert = authService.login(LoginRequest.builder()
                .email("expert@gmail.com")
                .password("12345678")
                .build());
        LoginResponse office = authService.login(LoginRequest.builder()
                .email("office@gmail.com")
                .password("12345678")
                .build());

        System.out.println("=======================Admin=======================");
        System.out.println(admin.toString());
        System.out.println("=======================User=======================");
        System.out.println(user.toString());
        System.out.println("=======================Expert=======================");
        System.out.println(expert.toString());
        System.out.println("=======================Office=======================");
        System.out.println(office.toString());

    }
}

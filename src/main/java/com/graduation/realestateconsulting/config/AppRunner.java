package com.graduation.realestateconsulting.config;

import com.graduation.realestateconsulting.model.dto.request.LoginRequest;
import com.graduation.realestateconsulting.model.dto.response.LoginResponse;
import com.graduation.realestateconsulting.model.entity.*;
import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import com.graduation.realestateconsulting.repository.*;
import com.graduation.realestateconsulting.services.AuthService;
import com.graduation.realestateconsulting.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ExpertRepository expertRepository;
    private final OfficeRepository officeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReportCategoryRepository reportCategoryRepository;

    private final AuthService authService;

    @Override
    public void run(String... args) throws Exception {

        if(reportCategoryRepository.count() == 0) {
            ReportCategory cat1 = createCategory("Problem involving someone under 18");
            ReportCategory cat2 = createCategory("Bullying, harassment or abuse");
            ReportCategory cat3 = createCategory("Suicide or self-harm");
            ReportCategory cat4 = createCategory("Violent, hateful or disturbing content");
            ReportCategory cat5 = createCategory("Selling or promoting restricted items");
            ReportCategory cat6 = createCategory("Adult content");
            ReportCategory cat7 = createCategory("Scam, fraud or false information");
            ReportCategory cat8 = createCategory("Intellectual property");
            ReportCategory cat9 = createCategory("I just don't like it");
            ReportCategory cat10 = createCategory("Other");

            reportCategoryRepository.saveAll(List.of(
                    cat1, cat2, cat3, cat4, cat5, cat6, cat7, cat8, cat9, cat10
            ));
        }

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

            User expert2 = userRepository.save(User.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .email("expert2@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .enable(true)
                    .role(Role.EXPERT)
                    .status(UserStatus.AVAILABLE)
                    .phone("0999999999")
                    .build());
            expertRepository.save(
                    Expert.builder()
                            .user(expert2)
                            .rateCount(1)
                            .experience("expert")
                            .profession("expert")
                            .idCardImage("expert")
                            .degreeCertificateImage("expert")
                            .totalRate(5)
                            .build()
            );


            User expert3 = userRepository.save(User.builder()
                    .firstName("firstName")
                    .lastName("lastName")
                    .email("expert3@gmail.com")
                    .password(passwordEncoder.encode("12345678"))
                    .enable(true)
                    .role(Role.EXPERT)
                    .status(UserStatus.AVAILABLE)
                    .phone("0999999999")
                    .build());
            expertRepository.save(
                    Expert.builder()
                            .user(expert3)
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
        LoginResponse expert2 = authService.login(LoginRequest.builder()
                .email("expert2@gmail.com")
                .password("12345678")
                .build());
        LoginResponse expert3 = authService.login(LoginRequest.builder()
                .email("expert3@gmail.com")
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
        System.out.println(expert2.toString());
        System.out.println(expert3.toString());
        System.out.println("=======================Office=======================");
        System.out.println(office.toString());

    }

    private ReportCategory createCategory(String title) {
        ReportCategory category = new ReportCategory();
        category.setTitle(title);
        return category;
    }
}

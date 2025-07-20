package com.graduation.realestateconsulting.model.mapper;


import com.graduation.realestateconsulting.model.entity.*;
import com.graduation.realestateconsulting.repository.*;
import com.graduation.realestateconsulting.services.ImageService;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Mapper
public abstract class AppMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private FaqCategoryRepository faqCategoryRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private RoomRepository roomRepository;


    @Autowired
    private ImageService imageService;

    @Value("${image.url}")
    private String imageUrlPrefix;

    @Named("encodePassword")
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Named("addPrefixToImageUrl")
    public String addPrefixToImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return "";
        }
        return imageUrlPrefix + imageUrl;
    }

    @Named("convertStringToListOfInteger")
    public List<Integer> convertStringToListOfInteger(String str) {
        if (str == null || str.isEmpty()) {
            return List.of();
        }

        String[] list = str.split(",");
        return Arrays.stream(list).map(Integer::parseInt).toList();
    }

    @Named("getUserById")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Named("getRoomById")
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }


    @Named("getClientById")
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }

    @Named("getOfficeById")
    public Office getOfficeById(Long id) {
        return officeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Office not found"));
    }

    @Named("getExpertById")
    public Expert getExpertById(Long id) {
        return expertRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expert not found"));
    }

    @Named("getPropertyById")
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Property not found"));
    }

    @Named("getFaqCategoryById")
    public FaqCategory getFaqCategoryById(Long id) {
        return faqCategoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Faq Category not found"));
    }

    @Named("uploadImage")
    public String uploadImage(MultipartFile file) throws IOException {
        return imageService.uploadImage(file);
    }

}

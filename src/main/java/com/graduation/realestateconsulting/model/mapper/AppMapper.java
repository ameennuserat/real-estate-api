package com.graduation.realestateconsulting.model.mapper;


import com.graduation.realestateconsulting.model.entity.Client;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.Office;
import com.graduation.realestateconsulting.model.entity.Property;
import com.graduation.realestateconsulting.repository.ClientRepository;
import com.graduation.realestateconsulting.repository.ExpertRepository;
import com.graduation.realestateconsulting.repository.OfficeRepository;
import com.graduation.realestateconsulting.repository.PropertyRepository;
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
    private ClientRepository clientRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ExpertRepository expertRepository;

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
        if(imageUrl == null){
            return "";
        }
        return imageUrlPrefix+imageUrl;
    }

    @Named("convertStringToListOfInteger")
    public List<Integer> convertStringToListOfInteger(String str) {
        if(str == null || str.isEmpty()){
            return List.of();
        }

        String[] list = str.split(",");
        return Arrays.stream(list).map(Integer::parseInt).toList();
    }

    @Named("getClientById")
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Client not found"));
    }

    @Named("getOfficeById")
    public Office getOfficeById(Long id) {
        return officeRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Office not found"));
    }

    @Named("getExpertById")
    public Expert getExpertById(Long id) {
        return expertRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Expert not found"));
    }

    @Named("getPropertyById")
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Property not found"));
    }

    @Named("uploadImage")
    public String uploadImage(MultipartFile file) throws IOException {
        return imageService.uploadImage(file);
    }

}

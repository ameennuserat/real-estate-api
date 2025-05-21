package com.graduation.realestateconsulting.model.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Mapper
public abstract class AppMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

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

}

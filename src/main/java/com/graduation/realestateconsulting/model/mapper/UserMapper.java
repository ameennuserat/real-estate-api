package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.RegisterRequest;
import com.graduation.realestateconsulting.model.dto.response.UserResponse;
import com.graduation.realestateconsulting.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {AppMapper.class})
public interface UserMapper {

    @Mapping(target = "imageUrl" , source = "imageUrl", qualifiedByName = "addPrefixToImageUrl")
      UserResponse toDto(User entity);
      List<UserResponse> toDtos(List<User> entities);

    // for save entity
    @Mapping(target = "enable" ,constant = "false")
    @Mapping(target = "verificationCode", expression = "java(org.apache.commons.lang3.RandomStringUtils.randomNumeric(6))")
    @Mapping(target = "password" , source = "password", qualifiedByName = "encodePassword")
      User toEntity(RegisterRequest request);

//    // for update entity
//    @Mapping(target = "password" , source = "password", qualifiedByName = "encodePassword")
//    void toEntity(@MappingTarget User user, RegisterRequest request);

//    public User toEntity(RegisterRequest registerRequest) {
//        System.out.println("RegisterMapper toEntity");
//        return User.builder()
//                .email(registerRequest.getEmail())
//                .password(registerRequest.getPassword())
//                .phone(registerRequest.getPhone())
//                .verificationCode(RandomStringUtils.randomNumeric(6))
//                .role(registerRequest.getRole())
//                .enable(false)
//                .firstName(registerRequest.getFirstName())
//                .lastName(registerRequest.getLastName())
//                .build();
//    }
}

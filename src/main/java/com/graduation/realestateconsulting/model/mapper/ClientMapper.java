package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.response.ClientResponse;
import com.graduation.realestateconsulting.model.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {UserMapper.class,AppMapper.class})
public interface ClientMapper {

    @Mapping(target = "favorites",source = "favorites",qualifiedByName = "convertStringToListOfInteger")
    @Mapping(target = "following",source = "following",qualifiedByName = "convertStringToListOfInteger")
    ClientResponse toDto(Client entity);

    List<ClientResponse> toDtos(List<Client> entities);

}

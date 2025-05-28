package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.TicketRequest;
import com.graduation.realestateconsulting.model.dto.response.TicketResponse;
import com.graduation.realestateconsulting.model.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(uses = {AppMapper.class,ClientMapper.class})
public interface TicketMapper {

    TicketResponse toDto(Ticket entity);
    List<TicketResponse> toDtos(List<Ticket> entities);


    @Mapping(target ="client",source = "clientId" ,qualifiedByName = "getClientById")
    Ticket toEntity(TicketRequest request);
    @Mapping(target ="client",source = "clientId" ,qualifiedByName = "getClientById")
    void toEntity(@MappingTarget Ticket entity,TicketRequest request);

}

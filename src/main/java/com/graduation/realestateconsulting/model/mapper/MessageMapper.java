package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.MessageRequest;
import com.graduation.realestateconsulting.model.dto.response.MessageResponse;
import com.graduation.realestateconsulting.model.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {AppMapper.class,UserMapper.class})
public interface MessageMapper {

    MessageResponse toDto(Message entity);

    List<MessageResponse> toDtos(List<Message> entities);

    @Mapping(target = "sender",source = "senderId",qualifiedByName = "getUserById")
    @Mapping(target = "room",source = "roomId",qualifiedByName = "getRoomById")
    Message toEntity(MessageRequest dto);

}

package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.ChatMessageRequest;
import com.graduation.realestateconsulting.model.dto.response.MessageResponse;
import com.graduation.realestateconsulting.model.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {AppMapper.class,UserMapper.class})
public interface MessageMapper {

    @Mapping(target = "fileUrl",source = "filePath",qualifiedByName = "addPrefixToImageUrl")
    MessageResponse toDto(Message entity);

    List<MessageResponse> toDtos(List<Message> entities);

//    @Mapping(target = "sender",source = "senderId",qualifiedByName = "getUserById")
//    @Mapping(target = "room",source = "roomId",qualifiedByName = "getRoomById")
//    Message toEntity(ChatMessageRequest dto);

}

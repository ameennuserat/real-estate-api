package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.response.RoomResponse;
import com.graduation.realestateconsulting.model.entity.Room;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {UserMapper.class})
public interface RoomMapper {

    RoomResponse toDto(Room entity);

    List<RoomResponse> toDtos(List<Room> entities);

}

package com.graduation.realestateconsulting.model.mapper;


import com.graduation.realestateconsulting.model.dto.request.WorkingTimesRequest;
import com.graduation.realestateconsulting.model.dto.response.WorkingTimesResponse;
import com.graduation.realestateconsulting.model.entity.Expert;
import com.graduation.realestateconsulting.model.entity.WorkingTimes;
import org.mapstruct.*;
import java.util.List;

@Mapper(uses = {ExpertMapper.class})
public interface WorkingTimesMapper {
    WorkingTimesResponse toDto(WorkingTimes workingTimes);
    List<WorkingTimesResponse> toDto(List<WorkingTimes> workingTimes);
    @Mapping(source = "day", target = "dayOfWeek")
    WorkingTimes toEntity(WorkingTimesRequest workingTimesRequest);
    @Mapping(source = "day", target = "dayOfWeek")
    void toEntity(WorkingTimesRequest workingTimesRequest, @MappingTarget WorkingTimes workingTimes);
    @Mapping(source = "day", target = "dayOfWeek")
    List<WorkingTimes> toEntities(List<WorkingTimesRequest> requests, @Context Expert expert);

    @AfterMapping
    default void assignExpert(@MappingTarget WorkingTimes workingTimes, @Context Expert expert) {
        workingTimes.setExpert(expert);
    }
}

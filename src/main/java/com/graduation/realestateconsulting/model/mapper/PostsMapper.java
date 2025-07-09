package com.graduation.realestateconsulting.model.mapper;

import com.graduation.realestateconsulting.model.dto.request.PostsRequest;
import com.graduation.realestateconsulting.model.dto.response.PostsResponse;
import com.graduation.realestateconsulting.model.entity.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(uses = {AppMapper.class})
public interface PostsMapper {

    @Mapping(target = "expert.id",source = "expert.id")
    @Mapping(target = "expert.userId",source = "expert.user.id")
    @Mapping(target = "expert.firstName",source = "expert.user.firstName")
    @Mapping(target = "expert.lastName",source = "expert.user.lastName")
    @Mapping(target = "expert.email",source = "expert.user.email")
    @Mapping(target = "expert.phone",source = "expert.user.phone")
    @Mapping(target = "expert.imageUrl",source = "expert.user.imageUrl",qualifiedByName = "addPrefixToImageUrl")
    @Mapping(target = "expert.profession",source = "expert.profession")
    @Mapping(target = "expert.experience",source = "expert.experience")
    @Mapping(target = "expert.rateCount",source = "expert.rateCount")
    @Mapping(target = "expert.rating",expression = "java(expert.getTotalRate() / expert.getRateCount())")
    @Mapping(target = "imageUrl",source = "imageUrl",qualifiedByName = "addPrefixToImageUrl")
    PostsResponse toDto(Posts entity);

    List<PostsResponse> toDtos(List<Posts> entities);


    @Mapping(target ="expert",source = "expertId" ,qualifiedByName = "getExpertById")
    @Mapping(target ="imageUrl",source = "image" ,qualifiedByName = "uploadImage")
    Posts toEntity(PostsRequest request);

}

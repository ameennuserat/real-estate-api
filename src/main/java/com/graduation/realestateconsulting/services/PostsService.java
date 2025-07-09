package com.graduation.realestateconsulting.services;


import com.graduation.realestateconsulting.model.dto.request.PostsRequest;
import com.graduation.realestateconsulting.model.dto.response.PostsResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostsService {

    List<PostsResponse> findAll();

    List<PostsResponse> findForHome(Pageable pageable);

    List<PostsResponse> findAllByExpertId(Long expertId);

    PostsResponse findById(Long id);

    PostsResponse save(PostsRequest request);

    PostsResponse update(Long id ,String content);

    void delete(Long id);

}
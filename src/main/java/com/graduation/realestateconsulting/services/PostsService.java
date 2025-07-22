package com.graduation.realestateconsulting.services;


import com.graduation.realestateconsulting.model.dto.request.PostsRequest;
import com.graduation.realestateconsulting.model.dto.response.PostsResponse;
import com.graduation.realestateconsulting.model.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PostsService {

    List<PostsResponse> findAll();

    List<PostsResponse> findForHome(Pageable pageable);

    List<PostsResponse> findAllByExpertId(Long expertId);

    PostsResponse findById(Long id);

    PostsResponse save(PostsRequest request);

    PostsResponse update(Long id ,String content);

    void delete(Long id);

    Page<PostsResponse> filterPosts(Specification<Posts> postsSpecification, Pageable pageable);
}
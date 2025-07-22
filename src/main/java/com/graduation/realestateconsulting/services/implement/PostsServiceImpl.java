package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.PostsRequest;
import com.graduation.realestateconsulting.model.dto.response.PostsResponse;
import com.graduation.realestateconsulting.model.entity.Posts;
import com.graduation.realestateconsulting.model.mapper.PostsMapper;
import com.graduation.realestateconsulting.repository.PostsRepository;
import com.graduation.realestateconsulting.services.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository repository;
    private final PostsMapper mapper;

    @Override
    public List<PostsResponse> findAll() {
        return mapper.toDtos(repository.findAll());
    }

    @Override
    public List<PostsResponse> findForHome(Pageable pageable) {
        return repository.findByOrderByCreatedAtDesc(pageable)
                .map(mapper::toDto)
                .stream().toList();
    }

    @Override
    public List<PostsResponse> findAllByExpertId(Long expertId) {
        return mapper.toDtos(repository.findByExpertIdOrderByCreatedAtDesc(expertId));
    }

    @Override
    public PostsResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    @Override
    public PostsResponse save(PostsRequest request) {
        Posts post = mapper.toEntity(request);
        Posts saved = repository.save(post);
        return mapper.toDto(saved);
    }

    @Override
    public PostsResponse update(Long id, String content) {
        Posts post = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setContent(content);
        Posts saved = repository.save(post);
        return mapper.toDto(saved);
    }


    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<PostsResponse> filterPosts(Specification<Posts> postsSpecification, Pageable pageable) {
        return repository.findAll(postsSpecification, pageable).map(mapper::toDto);
    }
}
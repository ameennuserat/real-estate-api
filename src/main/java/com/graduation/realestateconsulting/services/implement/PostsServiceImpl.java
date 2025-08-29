package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.dto.request.PostsRequest;
import com.graduation.realestateconsulting.model.dto.response.PostsResponse;
import com.graduation.realestateconsulting.model.entity.Posts;
import com.graduation.realestateconsulting.model.mapper.PostsMapper;
import com.graduation.realestateconsulting.repository.PostsRepository;
import com.graduation.realestateconsulting.services.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository repository;
    private final PostsMapper mapper;
    private final CacheManager cacheManager;

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

    @Cacheable(value = "expertPosts", key = "#expertId")
    @Override
    public List<PostsResponse> findAllByExpertId(Long expertId) {
        return mapper.toDtos(repository.findByExpertIdOrderByCreatedAtDesc(expertId));
    }

    @Cacheable(value = "posts", key = "#id")
    @Override
    public PostsResponse findById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    @CacheEvict(value = "expertPosts", key = "#result.expert.id")
    @Override
    public PostsResponse save(PostsRequest request) {
        Posts post = mapper.toEntity(request);
        Posts saved = repository.save(post);
        return mapper.toDto(saved);
    }

    @Caching(
            put = { @CachePut(value = "posts", key = "#id") },
            evict = { @CacheEvict(value = "expertPosts", key = "#result.expert.id") }
    )
    @Override
    public PostsResponse update(Long id, String content) {
        Posts post = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setContent(content);
        Posts saved = repository.save(post);
        return mapper.toDto(saved);
    }


    @Override
    public void delete(Long id) {
        Posts post = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        Long expertId = post.getExpert().getId();

        repository.delete(post);
        Cache postsCache = cacheManager.getCache("posts");
        if (postsCache != null) {
            postsCache.evict(id);
        }
        Cache expertPostsCache = cacheManager.getCache("expertPosts");
        if (expertPostsCache != null) {
            expertPostsCache.evict(expertId);
        }
        log.info("Post {} deleted. Evicted from 'posts' and 'expertPosts' caches.", id);
    }

    @Override
    public Page<PostsResponse> filterPosts(Specification<Posts> postsSpecification, Pageable pageable) {
        return repository.findAll(postsSpecification, pageable).map(mapper::toDto);
    }
}
package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    List<Posts> findByExpertIdOrderByCreatedAtDesc(Long expertId);

    Page<Posts> findByOrderByCreatedAtDesc(Pageable pageable);
}
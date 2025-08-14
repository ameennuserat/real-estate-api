package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> , JpaSpecificationExecutor<Posts> {
    List<Posts> findByExpertIdOrderByCreatedAtDesc(Long expertId);

    Page<Posts> findByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT COUNT(u) FROM Posts u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
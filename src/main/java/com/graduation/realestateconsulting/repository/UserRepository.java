package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String username);
    List<User> findByIsBlockedTrueAndBlockExpiresAtBefore(LocalDateTime dateTime);
}
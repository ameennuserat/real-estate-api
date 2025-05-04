package com.graduation.realestateconsulting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.graduation.realestateconsulting.model.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
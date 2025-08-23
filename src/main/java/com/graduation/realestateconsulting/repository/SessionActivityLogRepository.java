package com.graduation.realestateconsulting.repository;

import com.graduation.realestateconsulting.model.entity.SessionActivityLog;
import com.graduation.realestateconsulting.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SessionActivityLogRepository extends JpaRepository<SessionActivityLog, Long> {
    List<SessionActivityLog>  findByBookingIdAndRoleInSessionOrderByEventTimestampAsc(Long bookingId, Role role);
}

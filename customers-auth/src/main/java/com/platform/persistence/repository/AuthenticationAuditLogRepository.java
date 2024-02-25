package com.platform.persistence.repository;

import com.platform.persistence.entity.AuthenticationAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationAuditLogRepository extends JpaRepository<AuthenticationAuditLog, Long> {

  List<AuthenticationAuditLog> findByClientUsername(String clientUsername);
}

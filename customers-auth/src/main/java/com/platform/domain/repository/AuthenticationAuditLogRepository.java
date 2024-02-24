package com.platform.domain.repository;

import com.platform.domain.entity.AuthenticationAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationAuditLogRepository extends JpaRepository<AuthenticationAuditLog, Long> {

  List<AuthenticationAuditLog> findByClientUsername(String clientUsername);

}

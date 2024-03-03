package com.platform.persistence.repository;

import com.platform.persistence.entity.AuthenticationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthenticationLogRepository extends JpaRepository<AuthenticationLogEntity, Long> {

  List<AuthenticationLogEntity> findByClientUsername(String clientUsername);
}

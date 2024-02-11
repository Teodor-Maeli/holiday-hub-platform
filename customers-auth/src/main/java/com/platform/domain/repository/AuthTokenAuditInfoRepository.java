package com.platform.domain.repository;

import com.platform.domain.entity.AuthTokenAuditInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenAuditInfoRepository extends JpaRepository<AuthTokenAuditInfo, Long> {

}

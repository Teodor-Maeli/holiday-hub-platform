package com.platform.authorizationserver.domain.repository;

import com.platform.authorizationserver.domain.entity.PlatformClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformClientRepository extends JpaRepository<PlatformClient, Long> {

}

package com.platform.authorizationserver.domain.repository;

import com.platform.authorizationserver.domain.entity.LegalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalEntityRepository extends JpaRepository<LegalEntity, Long> {

}

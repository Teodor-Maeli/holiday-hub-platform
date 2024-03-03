package com.platform.persistence.repository;

import com.platform.persistence.entity.CompanyEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends BaseClientRepository<CompanyEntity, Long> {

  CompanyEntity findByRepresentativesUsername(String username);
}

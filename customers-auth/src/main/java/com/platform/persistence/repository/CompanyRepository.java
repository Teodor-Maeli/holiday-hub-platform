package com.platform.persistence.repository;

import com.platform.persistence.BaseClientRepository;
import com.platform.persistence.entity.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends BaseClientRepository<Company, Long> {

  Company findByRepresentativesUsername(String username);
}

package com.platform.persistence.repository;

import com.platform.persistence.entity.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CustomerRepository<Company, Long> {

  Company findByRepresentativesUsername(String username);
}

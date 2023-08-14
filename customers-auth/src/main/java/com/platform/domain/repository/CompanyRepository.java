package com.platform.domain.repository;

import com.platform.domain.entity.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends BaseClientRepository<Company, Long> {
}

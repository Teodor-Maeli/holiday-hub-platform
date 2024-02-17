package com.platform.domain.repository;

import com.platform.domain.entity.CompanyRepresentative;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends BaseClientRepository<CompanyRepresentative, Long> {

}

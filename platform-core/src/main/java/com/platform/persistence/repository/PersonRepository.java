package com.platform.persistence.repository;

import com.platform.persistence.entity.PersonEntity;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PersonRepository extends BaseClientRepository<PersonEntity, Long> {

  Set<PersonEntity> findByCompanyUsername(String username);
}

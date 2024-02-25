package com.platform.persistence.repository;

import com.platform.persistence.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PersonRepository extends BaseClientRepository<Person, Long> {

  Set<Person> findByCompanyUsername(String username);
}

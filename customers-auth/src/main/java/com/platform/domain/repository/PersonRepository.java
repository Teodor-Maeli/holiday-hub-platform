package com.platform.domain.repository;

import com.platform.domain.entity.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends BaseClientRepository<Person, Long> {

}

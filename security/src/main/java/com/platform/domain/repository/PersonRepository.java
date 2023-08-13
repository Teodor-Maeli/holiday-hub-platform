package com.platform.domain.repository;

import com.platform.domain.entity.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends PlatformBaseRepository<Person, Long> {

    @Query("SELECT p FROM Person p "
        + "LEFT JOIN FETCH p.roles r "
        + "WHERE p.username = :username")
    Optional<Person> findByUserName(@Param("username") String username);
}

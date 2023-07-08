package com.platform.domain.repository;

import com.platform.domain.entity.Person;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {

    @Query("SELECT P FROM Person P WHERE P.username = :username")
    Optional<Person> findByUserName(@Param("username") String username);
}

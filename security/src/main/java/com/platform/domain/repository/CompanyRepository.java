package com.platform.domain.repository;

import com.platform.domain.entity.Company;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("SELECT c FROM Company c "
        + "LEFT JOIN FETCH c.roles r "
        + "WHERE c.username = :username")
    Optional<Company> findByUserName(@Param("username") String username);
}

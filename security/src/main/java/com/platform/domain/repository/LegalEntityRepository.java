package com.platform.domain.repository;

import com.platform.domain.entity.LegalEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalEntityRepository extends JpaRepository<LegalEntity, Long> {

    @Query("SELECT le FROM LegalEntity le "
        + "LEFT JOIN FETCH le.roles r "
        + "WHERE le.username = :username")
    Optional<LegalEntity> findByUserName(@Param("username") String username);
}

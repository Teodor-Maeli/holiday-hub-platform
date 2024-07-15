package com.platform.persistence.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Base repository interface, extend and override if needed.
 */
@NoRepositoryBean
public interface CustomerRepository<T, ID extends Number> extends JpaRepository<T, ID> {

  @Query("""
      SELECT e FROM #{#entityName} e
      LEFT JOIN FETCH e.authenticationAttempts attempts
      WHERE e.username = :username
      """)
  Optional<T> findByUsernameForAuthentication(@Param("username") String username);

  boolean existsByUsername(String username);

  @Transactional
  @Modifying
  @Query("DELETE #{#entityName} e WHERE e.username = :username")
  int deleteByUserName(String username);

  @Override
  <S extends T> S save(S entity);
}

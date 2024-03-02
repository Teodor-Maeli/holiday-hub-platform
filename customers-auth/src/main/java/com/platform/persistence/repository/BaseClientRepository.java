package com.platform.persistence.repository;

import com.platform.persistence.entity.Client;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Base repository interface, override if needed.
 *
 * @param <E>  Entity
 * @param <ID> ID
 */
@NoRepositoryBean
public interface BaseClientRepository<E extends Client, ID> extends JpaRepository<E, ID> {

  @Query("""
      SELECT e FROM #{#entityName} e
      LEFT JOIN FETCH e.authorities r
      LEFT JOIN FETCH e.authenticationAuditLogs t
      WHERE e.username = :username
      AND CASE
            WHEN t IS NULL THEN TRUE
            WHEN t.authenticationStatus NOT IN ('BLACKLISTED', 'LOCKED') THEN TRUE
            ELSE t.statusResolved
          END
      """)
  Optional<E> findByUsernameAndNotLockedOrBlacklisted(@Param("username") String username);

  @Query("""
      SELECT e FROM #{#entityName} e
      LEFT JOIN FETCH e.authorities r
      WHERE e.username = :username
      """)
  Optional<E> findByUsername(@Param("username") String username);

  boolean existsByUsername(String username);

  @Transactional
  @Modifying
  @Query("DELETE #{#entityName} e WHERE e.username = :username")
  int deleteByUserName(String username);

  @Transactional
  @Modifying
  @Query("""
      UPDATE #{#entityName} e
      SET e.password = :password
      WHERE e.username = :username
      """)
  int updatePasswordByUsername(@Param("username") String username,
                               @Param("password") String password);

}

package com.platform.persistence.repository;

import com.platform.persistence.entity.AuthenticationLogEntity;
import com.platform.persistence.entity.ClientEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Base repository interface, extend and override if needed.
 *
 * @param <E>  Entity
 * @param <ID> ID
 */
@NoRepositoryBean
public interface BaseClientRepository<E extends ClientEntity, ID extends Number> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

  @Query("""
      SELECT e FROM #{#entityName} e
      LEFT JOIN FETCH e.authenticationLogs logs
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

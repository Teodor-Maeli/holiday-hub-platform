package com.platform.domain.repository;

import com.platform.domain.entity.Client;
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
 * @author Teodor Maeli
 */
@NoRepositoryBean
public interface BaseClientRepository<E extends Client, ID> extends JpaRepository<E, ID> {

  @Query("""
      SELECT e FROM #{#entityName} e
      LEFT JOIN FETCH e.roles r
      JOIN FETCH e.sessions s
      WHERE e.username = :username
      AND s.active = true
       """)
  Optional<E> findByUserNameAndActiveSession(@Param("username") String username);

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

  @Transactional
  @Modifying
  @Query("""
      UPDATE #{#entityName} e
      SET e.enabled = :enabled
      WHERE e.username = :username
          """)
  int disableOrEnableByUsername(@Param("username") String username,
                                @Param("enabled") Boolean enabled);

}

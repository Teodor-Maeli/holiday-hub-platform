package com.platform.persistence.repository;

import com.platform.persistence.entity.AuthenticationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuthenticationAttemptRepository extends JpaRepository<AuthenticationAttempt, Long> {

  List<AuthenticationAttempt> findByCustomerId(Long id);

  @Query("""
      SELECT e FROM #{#entityName} e
      LEFT JOIN FETCH e.customer customer
      WHERE customer.username = :username
      AND (e.statusResolved = FALSE
      OR (e.createdDate between :startDate AND :endDate
      AND e.statusReason = BAD_CREDENTIALS))
      """)
  List<AuthenticationAttempt> finAutoLockedByUsername(@Param("username") String username,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);
}

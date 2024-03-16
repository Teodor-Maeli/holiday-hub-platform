package com.platform.persistence.repository;

import com.platform.persistence.entity.ClientEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class AuthenticationSpecification {

  public static <E extends ClientEntity> Specification<E> getForLogin(String username) {
    return (root, query, criteriaBuilder) -> {
      Predicate client = criteriaBuilder.equal(root.get("username"), username);

      @SuppressWarnings("unchecked")
      Join<Object, Object> authenticationLogsJoin = (Join<Object, Object>) root.fetch("authenticationLogs", JoinType.LEFT);
      Join<Object, Object> statusResolved = authenticationLogsJoin.on(criteriaBuilder.equal(authenticationLogsJoin.get("statusResolved"), false));

      Predicate joined = criteriaBuilder.and(statusResolved.getOn());
      Predicate clientAndLogs = criteriaBuilder.and(client, joined);

      return criteriaBuilder.or(clientAndLogs, client);
    };
  }


}

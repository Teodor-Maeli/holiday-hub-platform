package com.platform.util;

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
      @SuppressWarnings("unchecked")
      Join<Object, Object> consumerAuthoritiesJoin = (Join<Object, Object>) root.fetch("consumerAuthorities", JoinType.LEFT);
      Join<Object, Object> statusResolvedJoin = authenticationLogsJoin.on(criteriaBuilder.equal(authenticationLogsJoin.get("statusResolved"), false));
      Join<Object, Object> clientAuthoritiesJoin = consumerAuthoritiesJoin.on(criteriaBuilder.equal(root.get("username"), username));


      Predicate statusResolved = criteriaBuilder.and(statusResolvedJoin.getOn());
      Predicate clientWithAuthorities = criteriaBuilder.and(client, clientAuthoritiesJoin.getOn());
      Predicate clientWithAuthoritiesAndLogs = criteriaBuilder.and(clientWithAuthorities, statusResolved);

      return criteriaBuilder.or(clientWithAuthoritiesAndLogs, clientWithAuthorities);
    };
  }


}

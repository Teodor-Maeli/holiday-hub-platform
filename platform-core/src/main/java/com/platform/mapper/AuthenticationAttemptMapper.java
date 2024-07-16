package com.platform.mapper;

import com.platform.model.AuthenticationAttemptResource;
import com.platform.persistence.entity.AuthenticationAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthenticationAttemptMapper {

  List<AuthenticationAttemptResource> toResource(List<AuthenticationAttempt> attempts);

  AuthenticationAttemptResource toResource(AuthenticationAttempt attempt);

  @Mapping(target = "customer", ignore = true)
  AuthenticationAttempt toEntity(AuthenticationAttemptResource resource);
}

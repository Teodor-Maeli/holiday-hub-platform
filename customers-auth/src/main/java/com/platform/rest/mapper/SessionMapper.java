package com.platform.rest.mapper;

import com.platform.domain.entity.Session;
import com.platform.rest.resource.SessionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionMapper {

  @Mapping(source = "client.username", target = "clientUsername")
  @Mapping(source = "client.id", target = "clientId")
  SessionResponse toResponse(Session entity);
}

package com.platform.rest.mapper;

import com.platform.domain.entity.CompanyRepresentative;
import com.platform.rest.resource.CompanyRequest;
import com.platform.rest.resource.CompanyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class
    }
)
public interface CompanyMapper {

  CompanyResponse toResponse(CompanyRepresentative entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  CompanyRepresentative toEntity(CompanyRequest request);
}

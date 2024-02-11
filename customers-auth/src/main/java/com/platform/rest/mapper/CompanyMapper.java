package com.platform.rest.mapper;

import com.platform.domain.entity.Company;
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

  CompanyResponse toResponse(Company entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  Company toEntity(CompanyRequest request);
}

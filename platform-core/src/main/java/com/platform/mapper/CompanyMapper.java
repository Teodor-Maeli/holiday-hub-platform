package com.platform.mapper;

import com.platform.model.CompanyResource;
import com.platform.persistence.entity.Company;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class,
        AuthenticationAttemptMapper.class
    },
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CompanyMapper {

  @Mapping(target = "password", ignore = true)
  CompanyResource toResource(Company entity);

  @Mapping(target = "password", source = "password", qualifiedBy = EncodedMapping.class)
  Company toEntity(CompanyResource request);
}

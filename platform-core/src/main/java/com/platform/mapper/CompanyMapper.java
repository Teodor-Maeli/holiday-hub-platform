package com.platform.mapper;

import com.platform.model.CompanyResource;
import com.platform.persistence.entity.Company;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

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

  @Mapping(target = "type", constant = "COMPANY")
  CompanyResource toResource(Company entity);

  @Mapping(target = "password", source = "password", qualifiedBy = EncodedMapping.class)
  Company toEntityCreate(CompanyResource request);

  @Named(value = "toEntityUpdate")
  Company toEntityUpdate(CompanyResource request);

  @AfterMapping
  default void afterMapping(@MappingTarget Company company) {
    company.getConfiguration().setCustomer(company);
  }
}

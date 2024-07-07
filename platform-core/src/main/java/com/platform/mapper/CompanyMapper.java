package com.platform.mapper;

import com.platform.model.CompanyResponse;
import com.platform.model.registration.CompanyRegisterRequest;
import com.platform.persistence.entity.Company;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class
    },
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CompanyMapper {

  CompanyResponse toResponse(Company entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  Company toEntity(CompanyRegisterRequest request);
}

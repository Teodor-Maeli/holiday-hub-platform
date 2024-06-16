package com.platform.mapper;

import com.platform.model.Company;
import com.platform.model.registration.CompanyRegistration;
import com.platform.persistence.entity.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class
    }
)
public interface CompanyRepresentativeMapper {

  Company toResponse(CompanyEntity entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  CompanyEntity toEntity(CompanyRegistration request);
}

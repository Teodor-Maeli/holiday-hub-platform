package com.platform.mapper;

import com.platform.persistence.entity.CompanyEntity;
import com.platform.model.CompanyRegistration;
import com.platform.model.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
        SubscriptionMapper.class,
        PasswordEncoderMapper.class
    }
)
public interface CompanyRepresentativeMapper extends LazyLoadingAwareMapper {

  Company toResponse(CompanyEntity entity);

  @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
  CompanyEntity toEntity(CompanyRegistration request);
}

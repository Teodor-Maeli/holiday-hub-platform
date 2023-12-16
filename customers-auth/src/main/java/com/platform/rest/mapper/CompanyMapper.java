package com.platform.rest.mapper;

import com.platform.domain.entity.Company;
import com.platform.rest.resource.CompanyRequest;
import com.platform.rest.resource.CompanyResponse;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {
        SessionMapper.class,
        SubscriptionMapper.class
    }
)
public interface CompanyMapper {

  CompanyResponse toResponse(Company entity);
  Company toEntity(CompanyRequest request);
}

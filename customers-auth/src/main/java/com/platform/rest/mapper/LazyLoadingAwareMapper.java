package com.platform.rest.mapper;

import org.hibernate.Hibernate;
import org.mapstruct.Condition;

import java.util.Collection;

public interface LazyLoadingAwareMapper {

  @Condition
  default boolean isLazyLoaded(Collection<?> sourceCollection){
    return sourceCollection != null && Hibernate.isInitialized(sourceCollection);
  }

}

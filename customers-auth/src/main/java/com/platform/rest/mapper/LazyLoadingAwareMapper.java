package com.platform.rest.mapper;

import org.hibernate.Hibernate;
import org.mapstruct.Condition;

import java.util.Collection;

public interface LazyLoadingAwareMapper {

  @Condition
  default boolean isNotLazyLoaded(Collection<?> sourceCollection){
   return Hibernate.isInitialized(sourceCollection);
  }

}

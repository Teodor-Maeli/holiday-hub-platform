package com.platform.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Base response class.
 * Since 1.0
 */

@Setter
@Getter
@JsonInclude(Include.NON_NULL)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PersonResource.class, name = "PERSON"),
    @JsonSubTypes.Type(value = CustomerResource.class, name = "COMPANY")
})
public class CustomerResource {

  private Long id;
  private String username;
  private String password;
  private String phoneNumber;
  private String emailAddress;
  private Boolean locked;
  private CustomerType type;
  private LocalDateTime createdOn;
  private LocalDateTime updatedOn;
  private ConfigurationResource configuration;
  private Set<ConsumerAuthority> authorities;
  private List<AuthenticationAttemptResource> authenticationAttempts;
  private List<SubscriptionResource> subscriptions;
}

package com.platform.rest.controller;

import com.platform.aspect.audit.Audited;
import com.platform.model.CustomerResource;
import com.platform.model.CustomerType;
import com.platform.service.CustomerService;
import com.platform.service.DecoratingOptions;
import com.platform.service.ServiceResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Audited
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/customers/v1", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomersController {

  private final ServiceResolver resolver;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(path = "/create")
  public CustomerResource create(@RequestBody CustomerResource resource) {
    return resolver.resolve(resource.getType()).create(resource);
  }

  @ResponseStatus(HttpStatus.OK)
  @PostMapping(path = "/update")
  public CustomerResource update(@RequestBody CustomerResource resource) {
    return resolver.resolve(resource.getType()).update(resource);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/retrieve/{type}/{username}")
  @PreAuthorize("#username == authentication.principal")
  public CustomerResource retrieve(@RequestParam(required = false) Set<DecoratingOptions> include,
                                   @PathVariable String username,
                                   @PathVariable CustomerType type) {
    CustomerService service = resolver.resolve(type);

    return CollectionUtils.isEmpty(include)
           ? service.retrieve(username)
           : service.retrieve(include, username);
  }

}

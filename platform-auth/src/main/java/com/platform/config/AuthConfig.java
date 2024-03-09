package com.platform.config;

import com.platform.service.ClientAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration and beans initialization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class AuthConfig {

  private static final RequestMatcher registerMatcher =
      request -> request.getRequestURI().contains("/register");

  @Value("${platform.security.cors-config.allowedHeaders}")
  private String allowedHeaders;

  @Value("${platform.security.cors-config.allowedOrigins}")
  private String allowedOrigins;

  @Value("${platform.security.cors-config.allowedMethods}")
  private String allowedMethods;

  @Value("${platform.security.cors-config.path-mappings}")
  private String pathMapping;

  @Bean
  @DependsOn({"clientAuthService", "passwordEncoder"})
  public AuthenticationManager authenticationManager(HttpSecurity http, ClientAuthService authService, PasswordEncoder encoder) throws Exception {

    return http
        .getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(initDAOAuthProvider(authService, encoder))
        .build();
  }

  @Bean
  @DependsOn({"initAuthenticationFilter", "processAuthenticationFilter"})
  public SecurityFilterChain configuration(
      HttpSecurity http,
      InitAuthenticationFilter initAuthenticationFilter,
      ProcessAuthenticationFilter processAuthenticationFilter
  ) throws Exception {

    http.csrf().disable();
    http.cors();
    http.authorizeHttpRequests().requestMatchers(registerMatcher).permitAll();
    http.authorizeHttpRequests().anyRequest().authenticated();
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilter(initAuthenticationFilter);
    http.addFilterBefore(processAuthenticationFilter, InitAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public WebMvcConfigurer corsConfiguration() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping(pathMapping)
            .allowedOrigins(allowedOrigins)
            .allowedHeaders(allowedHeaders)
            .allowedMethods(allowedMethods);
      }
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  private AuthenticationProvider initDAOAuthProvider(UserDetailsService service, PasswordEncoder encoder) {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setPasswordEncoder(encoder);
    authProvider.setUserDetailsService(service);
    return authProvider;
  }

}

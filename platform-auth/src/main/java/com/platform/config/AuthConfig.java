package com.platform.config;

import com.platform.service.AuthService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration and beans initialization.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class AuthConfig {

  private final PlatformSecurityProperties properties;

  @Bean
  @DependsOn({"authService", "passwordEncoder"})
  public AuthenticationManager authenticationManager(HttpSecurity http, AuthService authService, PasswordEncoder encoder) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(initDAOAuthProvider(authService, encoder))
        .build();
  }

  @Bean
  @DependsOn({
      "authenticationManager",
      "platformAuthenticationFailureHandler",
      "platformAuthenticationSuccessHandler"
  })
  public SecurityFilterChain configuration(HttpSecurity http,
                                           AuthenticationManager authenticationManager,
                                           PlatformAuthenticationFailureHandler failureHandler,
                                           PlatformAuthenticationSuccessHandler successHandler) throws Exception {

    http.csrf().disable();
    http.cors();
    http.authorizeHttpRequests(matchers -> matchers
        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
        .requestMatchers(properties.getAllowedPaths()).permitAll()
        .anyRequest().denyAll());

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilter(new PlatformAuthenticationFilter(authenticationManager, failureHandler, successHandler));
    http.addFilterBefore(new PlatformAuthorizationFilter(), PlatformAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public WebMvcConfigurer corsConfiguration() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping(properties.getCorsPathMappings())
            .allowedOrigins(properties.getCorsAllowedOrigins())
            .allowedHeaders(properties.getCorsAllowedHeaders())
            .allowedMethods(properties.getCorsAllowedMethods());
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

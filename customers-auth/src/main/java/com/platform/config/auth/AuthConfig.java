package com.platform.config.auth;

import com.platform.service.CompanyService;
import com.platform.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 10.06.2023.
 *
 * <p>Configuration of Spring security. Since 1.0
 *
 * <p>Author : Teodor Maeli
 */
@Configuration
@EnableWebSecurity
public class AuthConfig {

    private static final RequestMatcher registerMatcher =
        request -> request.getRequestURI().contains("/register");

    @Bean
    @DependsOn({"personService", "companyService", "passwordEncoder"})
    public AuthenticationManager authenticationManager(HttpSecurity http, PersonService personService,
        CompanyService companyService, PasswordEncoder encoder) throws Exception {

        return http
            .getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(initDAOAuthProvider(personService, encoder))
            .authenticationProvider(initDAOAuthProvider(companyService, encoder))
            .build();
    }

    @Bean
    @DependsOn({"sessionRegistry"})
    public SecurityFilterChain configuration(HttpSecurity http, SessionRegistry sessionRegistry,
        StatefulAuthenticationFilter statefulAuthenticationFilter) throws Exception {

        http.csrf().disable().cors();
        http.authorizeHttpRequests().requestMatchers(registerMatcher).permitAll();
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).maximumSessions(1).sessionRegistry(sessionRegistry);
        http.addFilter(statefulAuthenticationFilter);

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public WebMvcConfigurer corsConfiguration() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // That's going to be changed soon.
                registry
                    .addMapping("/**")
                    .allowedOrigins("*")
                    .allowedHeaders("*")
                    .allowedMethods("*");
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

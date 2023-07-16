package com.platform.config.auth;

import com.platform.model.AuthRequestAction;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 10.06.2023.
 *
 * <p>Configuration of Spring security.</p>
 * Since 1.0
 *
 * <p>Author : Teodor Maeli</p>
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class AuthConfig {

    private SessionRegistry sessionRegistry;
    private static final RequestMatcher gatewayMatcher = (request) -> {
        String action = request.getParameter("action");

        return (AuthRequestAction.ADMIN_ENTITY_REGISTER.name().equalsIgnoreCase(action)
            || AuthRequestAction.PERSON_REGISTER.name().equalsIgnoreCase(action)
            || AuthRequestAction.PERSON_LOGIN.name().equalsIgnoreCase(action)
            || AuthRequestAction.ENTITY_LOGIN.name().equalsIgnoreCase(action)
            || AuthRequestAction.CLIENT_LOGOUT.name().equalsIgnoreCase(action));
    };

    @Bean
    public SecurityFilterChain configuration(HttpSecurity http) throws Exception {

        http
            .csrf().disable().cors()
            .and()
            .authorizeHttpRequests().requestMatchers(gatewayMatcher).permitAll()
            .and()
            .authorizeHttpRequests().anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).maximumSessions(2).sessionRegistry(sessionRegistry);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfiguration() {

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins("http://localhost:8080")
                    .allowedHeaders("*")
                    .allowedMethods("*");
            }
        };
    }
}

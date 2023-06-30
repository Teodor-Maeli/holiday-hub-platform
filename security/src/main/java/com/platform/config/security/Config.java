package com.platform.config.security;

import com.platform.model.HandlerAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
public class Config {

    private static final RequestMatcher gatewayMatcher = (request) -> {
        String action = request.getParameter("action");

        return (HandlerAction.ADMIN_ENTITY_REGISTER.name().equalsIgnoreCase(action)
            || HandlerAction.PERSON_REGISTER.name().equalsIgnoreCase(action)
            || HandlerAction.PERSON_LOGIN.name().equalsIgnoreCase(action)
            || HandlerAction.ENTITY_LOGIN.name().equalsIgnoreCase(action)
            || HandlerAction.CLIENT_LOGOUT.name().equalsIgnoreCase(action));
    };

    @Bean
    public SecurityFilterChain configuration(HttpSecurity http) throws Exception {

        http
            .csrf().disable().cors()
            .and()
            .authorizeHttpRequests().requestMatchers(gatewayMatcher).permitAll()
            .and()
            .authorizeHttpRequests().anyRequest().authenticated();

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

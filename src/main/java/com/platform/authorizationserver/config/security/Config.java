package com.platform.authorizationserver.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class Config {

    @Bean
    public SecurityFilterChain configuration(HttpSecurity http) throws Exception {

        http.csrf().disable().cors()
            .and()
            .authorizeHttpRequests().requestMatchers(getRegisterMatcher()).permitAll()
            .and()
            .authorizeHttpRequests().requestMatchers(getLoginMatcher()).permitAll()
            .and()
            .authorizeHttpRequests().anyRequest().authenticated();
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfiguration() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:8080")
                    .allowedHeaders("*")
                    .allowedMethods("*");
            }
        };
    }

    private RequestMatcher getLoginMatcher() {
        return (request) -> {
            String action = request.getParameter("action");
            return action != null && action.contains("LOGIN");
        };
    }

    private RequestMatcher getRegisterMatcher() {
        return (request) -> {
            String action = request.getParameter("action");
            return action != null && action.contains("REGISTER");
        };
    }
}

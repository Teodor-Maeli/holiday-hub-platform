package com.platform.authorizationserver.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class Config {

    @Bean
    public SecurityFilterChain configuration(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .cors()
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/authorization/admin/v1/**")
            .permitAll()
            .and()
            .authorizeHttpRequests()
            .anyRequest()
            .authenticated();
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfiguration() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(
                        "/**") // TODO accept requests only from gateway and resource server.
                    .allowedOrigins("*")
                    .allowedHeaders("*").allowedMethods("*");
            }
        };
    }
}

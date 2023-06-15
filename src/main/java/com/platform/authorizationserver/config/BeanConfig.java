package com.platform.authorizationserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 27.05.2023.
 *
 * <p>Creates project related bean configurations.</p>
 * Since 1.0.
 * <p>Author : Teodor Maeli</p>
 */

@Configuration
public class BeanConfig {

    @Bean
    public ObjectMapper initObjectMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}

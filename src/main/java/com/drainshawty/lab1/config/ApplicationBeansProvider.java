package com.drainshawty.lab1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApplicationBeansProvider {
    @Bean
    public BCryptPasswordEncoder newPasswordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

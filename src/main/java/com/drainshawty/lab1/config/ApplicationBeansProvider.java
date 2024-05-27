package com.drainshawty.lab1.config;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Component
public class ApplicationBeansProvider {
    @Bean
    public BCryptPasswordEncoder newPasswordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

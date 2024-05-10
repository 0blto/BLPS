package com.drainshawty.lab1.config;

import com.drainshawty.lab1.model.User;
import com.drainshawty.lab1.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminInitializer {

    @Value("${admin.name}")
    String ADMIN_NAME;

    @Value("${admin.password}")
    String ADMIN_PASSWORD;

    @Value("${spring.mail.username}")
    String ADMIN_EMAIL;

    private final UserRepo userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(UserRepo userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
                User admin = User.builder()
                        .email(ADMIN_EMAIL)
                        .name(ADMIN_NAME)
                        .roles(Set.of(User.Role.ADMIN, User.Role.USER, User.Role.WORKER))
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .build();
                userRepository.save(admin);
            }
        };
    }
}

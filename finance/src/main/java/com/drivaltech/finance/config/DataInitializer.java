package com.drivaltech.finance.config;

import com.drivaltech.finance.user.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User(
                        "admin",
                        passwordEncoder.encode("123456"),
                        Role.ADMIN
                );

                userRepository.save(admin);
            }
        };
    }
}

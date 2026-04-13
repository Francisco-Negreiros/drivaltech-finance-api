package com.drivaltech.finance.config;

import com.drivaltech.finance.user.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByUsername("admin").isEmpty()) {

                User admin = new User(
                        "admin",
                        passwordEncoder.encode("123456"), // CORREÇÃO AQUI
                        Set.of(Role.ADMIN)
                );

                userRepository.save(admin);
            }

            if (userRepository.findByUsername("valter").isEmpty()) {

                User user = new User(
                        "valter",
                        passwordEncoder.encode("123456"), // PADRONIZADO
                        Set.of(Role.USER)
                );

                userRepository.save(user);
            }
        };
    }
}

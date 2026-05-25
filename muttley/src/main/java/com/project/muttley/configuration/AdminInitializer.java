package com.project.muttley.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.muttley.domain.user.User;
import com.project.muttley.repositories.UserRepository;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

  @Bean
  CommandLineRunner initAdmin(
      UserRepository repository,
      PasswordEncoder encoder) {

    return args -> {

      if (!repository.existsByEmail("admin@fatec.sp.gov.br")) {

        User admin = new User();

        admin.setEmail("admin@fatec.sp.gov.br");
        admin.setPassword(
            encoder.encode("admin123"));

        admin.setRole("ADMIN");

        repository.save(admin);

        System.out.println("Admin user created!");
      }
    };
  }
}
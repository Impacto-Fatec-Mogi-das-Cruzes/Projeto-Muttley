package com.project.muttley.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.muttley.domain.event.eventstatus.EventStatus;
import com.project.muttley.repositories.EventStatusRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class EventDataInitializer {

  private final EventStatusRepository eventStatusRepository;

  @Bean
  CommandLineRunner initEventStatuses() {
    return args -> {
      ensureStatus("DRAFT");
      ensureStatus("IN_PROGRESS");
      ensureStatus("FINALIZED");
    };
  }

  private void ensureStatus(String name) {
    if (eventStatusRepository.findByNameIgnoreCase(name).isEmpty()) {
      EventStatus status = new EventStatus();
      status.setName(name);
      eventStatusRepository.save(status);
    }
  }
}

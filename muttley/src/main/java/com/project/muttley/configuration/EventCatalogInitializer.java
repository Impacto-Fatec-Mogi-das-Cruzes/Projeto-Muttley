package com.project.muttley.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.muttley.domain.event.eventmodality.EventModality;
import com.project.muttley.domain.event.eventtype.EventType;
import com.project.muttley.repositories.EventModalityRepository;
import com.project.muttley.repositories.EventTypeRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class EventCatalogInitializer {

  private static final String[] MODALITIES = {
      "Híbrido",
      "Presencial",
      "Online"
  };

  private static final String[] TYPES = {
      "Seminário",
      "Jornada",
      "Congresso",
      "Simpósio",
      "Empresarial",
      "Workshop",
      "Palestra",
      "Show",
      "Festa",
      "Jogos",
      "Torneio",
      "Feira",
      "Exposição",
      "Encontro",
      "Meetup"
  };

  private final EventModalityRepository eventModalityRepository;
  private final EventTypeRepository eventTypeRepository;

  @Bean
  CommandLineRunner initEventCatalog() {
    return args -> {
      for (String name : MODALITIES) {
        ensureModality(name);
      }
      for (String name : TYPES) {
        ensureType(name);
      }
    };
  }

  private void ensureModality(String name) {
    if (eventModalityRepository.findByNameIgnoreCase(name).isEmpty()) {
      EventModality modality = new EventModality();
      modality.setName(name);
      eventModalityRepository.save(modality);
    }
  }

  private void ensureType(String name) {
    if (eventTypeRepository.findByNameIgnoreCase(name).isEmpty()) {
      EventType type = new EventType();
      type.setName(name);
      eventTypeRepository.save(type);
    }
  }
}

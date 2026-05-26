package com.project.muttley.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.project.muttley.domain.event.mapper.EventMapper;
import com.project.muttley.domain.participant.participanttype.ParticipantType;
import com.project.muttley.repositories.ParticipantTypeRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ParticipantDataInitializer {

  private final ParticipantTypeRepository participantTypeRepository;

  @Bean
  CommandLineRunner initParticipantTypes() {
    return args -> {
      ensureType(EventMapper.ROLE_LISTENER);
      ensureType(EventMapper.ROLE_ORGANIZER);
      ensureType(EventMapper.ROLE_SPEAKER);
      ensureType(EventMapper.ROLE_SPONSOR);
    };
  }

  private void ensureType(String name) {
    if (participantTypeRepository.findByNameIgnoreCase(name).isEmpty()) {
      ParticipantType type = new ParticipantType();
      type.setName(name);
      participantTypeRepository.save(type);
    }
  }
}

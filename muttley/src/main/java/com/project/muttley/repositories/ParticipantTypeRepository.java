package com.project.muttley.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.domain.participant.participanttype.ParticipantType;

public interface ParticipantTypeRepository extends JpaRepository<ParticipantType, UUID> {

  boolean existsByNameIgnoreCase(String name);

  Optional<ParticipantType> findByNameIgnoreCase(String name);
}

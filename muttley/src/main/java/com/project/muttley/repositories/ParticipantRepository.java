package com.project.muttley.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.domain.participant.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {

  boolean existsByCpf(String cpf);

  Optional<Participant> findByCpf(String cpf);
}

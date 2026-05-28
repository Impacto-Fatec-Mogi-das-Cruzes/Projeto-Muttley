package com.project.muttley.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.domain.participant.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {

  boolean existsByCpf(String cpf);

  Optional<Participant> findByCpf(String cpf);

  List<Participant> findAllByIdIn(List<UUID> ids);

  List<Participant> findAllByEmailIn(List<String> emails);

  Page<Participant> findByNameContainingIgnoreCase(
      String name,
      Pageable pageable);
}

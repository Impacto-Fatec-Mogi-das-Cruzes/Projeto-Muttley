package com.project.muttley.repository;

import com.project.muttley.model.Participant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByCpf(String cpf);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Page<Participant> findByCpfContaining(String cpf, Pageable pageable);

    Page<Participant> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
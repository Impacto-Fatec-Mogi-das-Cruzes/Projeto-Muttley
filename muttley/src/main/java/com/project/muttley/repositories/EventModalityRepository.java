package com.project.muttley.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.domain.event.eventmodality.EventModality;

public interface EventModalityRepository extends JpaRepository<EventModality, UUID> {

  List<EventModality> findAllByOrderByNameAsc();

  Optional<EventModality> findByNameIgnoreCase(String name);
}

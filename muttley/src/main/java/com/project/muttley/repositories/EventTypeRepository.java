package com.project.muttley.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.domain.event.eventtype.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, UUID> {

  List<EventType> findAllByOrderByNameAsc();

  Optional<EventType> findByNameIgnoreCase(String name);
}

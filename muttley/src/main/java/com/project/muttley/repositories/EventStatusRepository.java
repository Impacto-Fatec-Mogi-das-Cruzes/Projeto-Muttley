package com.project.muttley.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.muttley.domain.event.eventstatus.EventStatus;

public interface EventStatusRepository extends JpaRepository<EventStatus, UUID> {

  Optional<EventStatus> findByNameIgnoreCase(String name);
}

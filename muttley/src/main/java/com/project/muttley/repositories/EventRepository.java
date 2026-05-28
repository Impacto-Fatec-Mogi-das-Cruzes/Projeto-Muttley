package com.project.muttley.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.muttley.domain.event.Event;

public interface EventRepository extends JpaRepository<Event, UUID> {

  @Query("""
      SELECT e FROM Event e
      WHERE (:title IS NULL OR :title = '' OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')))
      """)
  Page<Event> findAllFiltered(@Param("title") String title, Pageable pageable);

  Page<Event> findByEventParticipantsParticipantId(
      UUID participantId,
      Pageable pageable);

  Page<Event> findByTitleContainingIgnoreCaseAndEventParticipantsParticipantId(
      String title,
      UUID participantId,
      Pageable pageable);
}

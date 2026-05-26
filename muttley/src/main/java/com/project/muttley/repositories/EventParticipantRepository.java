package com.project.muttley.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.muttley.domain.event.eventparticipant.EventParticipant;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {

  Optional<EventParticipant> findByEvent_IdAndParticipant_Cpf(UUID eventId, String cpf);

  Optional<EventParticipant> findByEvent_IdAndParticipant_Id(UUID eventId, UUID participantId);

  boolean existsByEvent_IdAndParticipant_Id(UUID eventId, UUID participantId);

  @Query("""
      SELECT ep FROM EventParticipant ep
      JOIN ep.participant p
      LEFT JOIN ep.participantType pt
      WHERE ep.event.id = :eventId
      AND (:role IS NULL OR pt.name = :role)
      """)
  Page<EventParticipant> findByEventWithFilters(
      @Param("eventId") UUID eventId,
      @Param("role") String role,
      Pageable pageable);

  List<EventParticipant> findByEvent_IdAndParticipantType_NameIn(
      UUID eventId,
      Collection<String> roleNames);

  void deleteByEvent_IdAndParticipantType_NameIn(UUID eventId, Collection<String> roleNames);

  void deleteByEvent_Id(UUID eventId);

  long countByEvent_Id(UUID eventId);

  long countByEvent_IdAndPresentTrue(UUID eventId);
}

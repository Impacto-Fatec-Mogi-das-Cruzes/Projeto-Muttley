package com.project.muttley.domain.event.eventparticipant;

import java.time.LocalDateTime;
import java.util.UUID;

import com.project.muttley.domain.base.AuditableEntity;
import com.project.muttley.domain.event.Event;
import com.project.muttley.domain.participant.Participant;
import com.project.muttley.domain.participant.participanttype.ParticipantType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = { "event_id", "participant_id" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventParticipant extends AuditableEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  @ManyToOne
  @JoinColumn(name = "participant_id", nullable = false)
  private Participant participant;

  @ManyToOne
  @JoinColumn(name = "participant_type_id")
  private ParticipantType participantType;

  private LocalDateTime registeredAt;
  private LocalDateTime checkInAt;
  private Boolean present;
}

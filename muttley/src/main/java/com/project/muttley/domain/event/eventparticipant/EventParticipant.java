package com.project.muttley.domain.event.eventparticipant;

import java.time.LocalDateTime;
import java.util.UUID;

import com.project.muttley.domain.event.Event;
import com.project.muttley.domain.participant.Participant;
import com.project.muttley.domain.participant.participanttype.ParticipantType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class EventParticipant {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne
  private Event event;

  @ManyToOne
  private Participant participant;

  @ManyToOne
  private ParticipantType participantType;

  private LocalDateTime registeredAt;
  private LocalDateTime checkInAt;
  private Boolean present;
}
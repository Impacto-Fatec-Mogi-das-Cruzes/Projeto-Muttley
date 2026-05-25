package com.project.muttley.domain.event.eventparticipant;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class EventParticipantId implements Serializable {

  private UUID eventId;
  private UUID participantId;

  public EventParticipantId() {
  }

  public EventParticipantId(UUID eventId, UUID participantId) {
    this.eventId = eventId;
    this.participantId = participantId;
  }

  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public UUID getParticipantId() {
    return participantId;
  }

  public void setParticipantId(UUID participantId) {
    this.participantId = participantId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof EventParticipantId that))
      return false;
    return Objects.equals(eventId, that.eventId)
        && Objects.equals(participantId, that.participantId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, participantId);
  }
}
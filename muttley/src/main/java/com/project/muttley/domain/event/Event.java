package com.project.muttley.domain.event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.project.muttley.domain.event.eventmodality.EventModality;
import com.project.muttley.domain.event.eventparticipant.EventParticipant;
import com.project.muttley.domain.event.eventstatus.EventStatus;
import com.project.muttley.domain.event.eventtype.EventType;
import com.project.muttley.domain.publictype.PublicType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
  @Id
  @GeneratedValue
  private UUID id;

  private String title;

  private LocalDate dateStart;
  private LocalTime hourStart;

  private LocalDate dateEnd;
  private LocalTime hourEnd;

  private Long points;

  @ManyToOne
  @JoinColumn(name = "modality_id")
  private EventModality modality;

  private String addressOrLink;

  private Integer capacity;

  @ManyToOne
  @JoinColumn(name = "eventtype_id")
  private EventType eventType;

  @OneToMany(mappedBy = "event")
  private List<EventParticipant> eventParticipants;

  private String subject;

  private String keywords;

  private String description;

  private String observations;
  private String requirements;

  @ManyToOne
  @JoinColumn(name = "publictype_id")
  private PublicType publicType;

  // Certificate Template

  private String imageSignatureUrl;

  private String imageBackgroundUrl;

  private String imageQrCodeInscriptionUrl;

  private String imageQrCodePresenceUrl;

  @ManyToOne
  @JoinColumn(name = "status_id")
  private EventStatus eventStatus;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}

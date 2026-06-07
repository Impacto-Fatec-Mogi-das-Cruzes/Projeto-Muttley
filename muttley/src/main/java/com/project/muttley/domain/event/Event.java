package com.project.muttley.domain.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com.project.muttley.domain.base.AuditableEntity;
import com.project.muttley.domain.event.eventmodality.EventModality;
import com.project.muttley.domain.event.eventparticipant.EventParticipant;
import com.project.muttley.domain.event.eventstatus.EventStatus;
import com.project.muttley.domain.event.eventtype.EventType;
import com.project.muttley.domain.publictype.PublicType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
  private UUID id;

  private String title;

  private LocalDate dateStart;

  private LocalTime hourStart;

  private LocalDate dateEnd;

  private LocalTime hourEnd;

  private Integer workLoad;

  private Integer points;

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

  @ManyToOne
  @JoinColumn(name = "publictype_id")
  private PublicType publicType;

  private String imageSignatureUrl;

  private String imageBackgroundUrl;

  private String nameSignature;

  private String positionSignature;

  private String imageQrCodeInscriptionUrl;

  private String imageQrCodePresenceUrl;

  @ManyToOne
  @JoinColumn(name = "status_id")
  private EventStatus eventStatus;
}

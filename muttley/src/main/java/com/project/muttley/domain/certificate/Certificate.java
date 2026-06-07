package com.project.muttley.domain.certificate;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com.project.muttley.domain.base.AuditableEntity;
import com.project.muttley.domain.event.Event;
import com.project.muttley.domain.participant.Participant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "certificate")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Certificate extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
  private UUID id;

  private String fileUrl;

  @ManyToOne
  @JoinColumn(name = "participant")
  private Participant participant;

  @ManyToOne
  @JoinColumn(name = "event")
  private Event event;
}

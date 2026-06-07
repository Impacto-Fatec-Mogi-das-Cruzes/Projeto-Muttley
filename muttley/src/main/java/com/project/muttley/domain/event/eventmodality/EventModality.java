package com.project.muttley.domain.event.eventmodality;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com.project.muttley.domain.base.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "event_modality")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventModality extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.VARCHAR)
  @Column(columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
  private UUID id;

  private String name;
}

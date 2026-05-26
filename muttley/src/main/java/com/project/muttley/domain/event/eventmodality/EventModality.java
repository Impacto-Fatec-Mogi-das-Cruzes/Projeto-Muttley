package com.project.muttley.domain.event.eventmodality;

import java.util.UUID;

import com.project.muttley.domain.base.AuditableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
  @GeneratedValue
  private UUID id;

  private String name;
}

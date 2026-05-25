package com.project.muttley.domain.medal;

import java.util.UUID;

import com.project.muttley.domain.participant.Participant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "medal")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Medal {

  @Id
  @GeneratedValue
  private UUID id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "participant_id")
  private Participant participant;

}

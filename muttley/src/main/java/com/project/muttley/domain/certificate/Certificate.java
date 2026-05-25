package com.project.muttley.domain.certificate;

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
@Table(name = "certificate")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Certificate {

  @Id
  @GeneratedValue
  private UUID id;

  private String fileUrl;

  @ManyToOne
  @JoinColumn(name = "participant")
  private Participant participant;

}

package com.project.muttley.domain.participant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.project.muttley.domain.competency.Competency;
import com.project.muttley.domain.participant.participanttype.ParticipantType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "participant")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Participant {

  @Id
  @GeneratedValue
  private UUID id;

  private String name;

  private String cpf;

  private String email;

  private String phoneNumber;

  private String institute;

  private String course;

  private Integer points;

  private String linkedinUrl;

  private Boolean allowImageUsage;

  @ManyToOne
  @JoinColumn(name = "participanttype_id")
  private ParticipantType participantType;

  @ManyToMany
  @JoinTable(name = "participant_competency", joinColumns = @JoinColumn(name = "participant_id"), inverseJoinColumns = @JoinColumn(name = "competency_id"))
  private List<Competency> competencies = new ArrayList<>();

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;
}

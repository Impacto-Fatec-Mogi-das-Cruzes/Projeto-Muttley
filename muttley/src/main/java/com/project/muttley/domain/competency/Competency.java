package com.project.muttley.domain.competency;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.project.muttley.domain.participant.Participant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "competency")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Competency {

  @Id
  @GeneratedValue
  private UUID id;

  private String name;

  @ManyToMany(mappedBy = "competencies")
  private List<Participant> participants = new ArrayList<>();
}

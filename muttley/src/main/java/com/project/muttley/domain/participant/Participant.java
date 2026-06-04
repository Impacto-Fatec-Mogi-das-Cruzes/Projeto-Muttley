package com.project.muttley.domain.participant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.project.muttley.domain.base.AuditableEntity;
import com.project.muttley.domain.certificate.Certificate;
import com.project.muttley.domain.competency.Competency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
public class Participant extends AuditableEntity {

  @Id
  @GeneratedValue
  private UUID id;

  private String name;

  @Column(unique = true)
  private String cpf;

  private String email;

  private Integer points;

  @ManyToMany
  @JoinTable(name = "participant_competency", joinColumns = @JoinColumn(name = "participant_id"), inverseJoinColumns = @JoinColumn(name = "competency_id"))
  private List<Competency> competencies = new ArrayList<>();

  @OneToMany(mappedBy = "participant")
  private List<Certificate> certificates = new ArrayList<>();

  // @OneToMany(mappedBy = "participant")
  // private List<Medal> medals = new ArrayList<>();
  private Integer medals;
}

package com.project.muttley.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDAL")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Medal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column
    String modality;

    @Column
    String competence;

    @Column
    String local;

    @Column
    Integer totalHours;

    @ManyToMany(mappedBy = "medals")
    private List<Competency> competencies;
}

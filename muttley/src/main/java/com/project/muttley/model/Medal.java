package com.project.muttley.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
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
    Integer totalHours;

    @Column
    String local;
}

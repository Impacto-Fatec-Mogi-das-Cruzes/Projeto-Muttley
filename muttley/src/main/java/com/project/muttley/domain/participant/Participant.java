package com.project.muttley.domain.participant;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.project.muttley.domain.participanttype.ParticipantType;

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

    private String linkedinUrl;

    private Boolean allowImageUsage;

    @ManyToOne
    @JoinColumn(name = "participanttype_id")
    private ParticipantType participantType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

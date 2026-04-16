package com.project.muttley.model;

import com.project.muttley.model.enums.IssuedMedalStatus;
import com.project.muttley.model.enums.PublicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "issued_medals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssuedMedal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_participation_id", nullable = false)
    private EventParticipation eventParticipation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medal_id", nullable = false)
    private Medal medal;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssuedMedalStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublicationStatus publicationStatus;
}
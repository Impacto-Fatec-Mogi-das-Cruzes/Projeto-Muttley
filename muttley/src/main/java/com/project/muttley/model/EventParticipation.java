package com.project.muttley.model;

import com.project.muttley.model.enums.ParticipationStatus;
import com.project.muttley.model.enums.RoleInEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "event_participations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_event_participation", columnNames = {"event_id", "participant_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleInEvent roleInEvent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParticipationStatus participationStatus;

    private LocalDateTime checkInAt;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.participationStatus == null) {
            this.participationStatus = ParticipationStatus.INTERESTED;
        }
        if (this.roleInEvent == null) {
            this.roleInEvent = RoleInEvent.LISTENER;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

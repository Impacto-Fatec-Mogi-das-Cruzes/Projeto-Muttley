package com.project.muttley.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.project.muttley.domain.eventmodality.EventModality;
import com.project.muttley.domain.eventstatus.EventStatus;
import com.project.muttley.domain.eventtype.EventType;
import com.project.muttley.domain.publictype.PublicType;

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
@Table(name = "event")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;

    private String description;

    private LocalDateTime dateStart;

    private LocalDateTime dateEnd;

    private String place;

    private String observations;

    private String requirements;

    private String imageAdvertisingUrl;

    private String imageQrCodeInscriptionUrl;

    private String imageQrCodePresenceUrl;

    private Boolean allowAdvertisingPublic;

    @ManyToOne
    @JoinColumn(name = "publictype_id")
    private PublicType publicType;

    @ManyToOne
    @JoinColumn(name = "eventtype_id")
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "modality_id")
    private EventModality modality;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private EventStatus eventStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

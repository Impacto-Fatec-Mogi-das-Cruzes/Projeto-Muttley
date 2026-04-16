package com.project.muttley.mapper;

import com.project.muttley.dto.participant.ParticipantRequestDTO;
import com.project.muttley.dto.participant.ParticipantResponseDTO;
import com.project.muttley.model.Participant;
import org.springframework.stereotype.Component;

@Component
public class ParticipantMapper {

    public void updateEntity(Participant participant, ParticipantRequestDTO request) {
        participant.setName(request.name());
        participant.setEmail(request.email());
        participant.setRa(request.ra());
        participant.setLinkedinUrl(request.linkedinUrl());
        participant.setGithub(request.github());
        participant.setParticipantType(request.participantType());
    }

    public ParticipantResponseDTO toResponse(Participant participant) {
        return new ParticipantResponseDTO(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getRa(),
                participant.getLinkedinUrl(),
                participant.getGithub(),
                participant.getParticipantType(),
                participant.getCreatedAt(),
                participant.getUpdatedAt()
        );
    }
}

package com.project.muttley.mapper;

import com.project.muttley.dto.participation.EventParticipationRequestDTO;
import com.project.muttley.dto.participation.EventParticipationResponseDTO;
import com.project.muttley.model.EventParticipation;
import org.springframework.stereotype.Component;

@Component
public class EventParticipationMapper {

    public void updateEntity(EventParticipation participation, EventParticipationRequestDTO request) {
        participation.setRoleInEvent(request.roleInEvent());
        participation.setParticipationStatus(request.participationStatus());
        participation.setCheckInAt(request.checkInAt());
        participation.setNotes(request.notes());
    }

    public EventParticipationResponseDTO toResponse(EventParticipation participation) {
        return new EventParticipationResponseDTO(
                participation.getId(),
                participation.getEvent().getId(),
                participation.getEvent().getName(),
                participation.getParticipant().getId(),
                participation.getParticipant().getName(),
                participation.getRoleInEvent(),
                participation.getParticipationStatus(),
                participation.getCheckInAt(),
                participation.getNotes(),
                participation.getCreatedAt(),
                participation.getUpdatedAt()
        );
    }
}

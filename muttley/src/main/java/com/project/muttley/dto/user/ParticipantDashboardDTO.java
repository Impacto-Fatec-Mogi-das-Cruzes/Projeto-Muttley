package com.project.muttley.dto.user;

import com.project.muttley.dto.medal.MedalResponseDTO;
import com.project.muttley.dto.participation.EventParticipationResponseDTO;
import com.project.muttley.dto.participant.ParticipantResponseDTO;

import java.util.List;

public record ParticipantDashboardDTO(
        ParticipantResponseDTO participant,
        List<EventParticipationResponseDTO> participations,
        List<MedalResponseDTO> medals
) {
}

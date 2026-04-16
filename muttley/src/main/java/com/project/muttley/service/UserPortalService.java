package com.project.muttley.service;

import com.project.muttley.dto.medal.MedalResponseDTO;
import com.project.muttley.dto.participation.EventParticipationResponseDTO;
import com.project.muttley.dto.participant.ParticipantResponseDTO;
import com.project.muttley.dto.user.ParticipantDashboardDTO;
import com.project.muttley.mapper.EventParticipationMapper;
import com.project.muttley.mapper.MedalMapper;
import com.project.muttley.mapper.ParticipantMapper;
import com.project.muttley.model.EventParticipation;
import com.project.muttley.model.Medal;
import com.project.muttley.model.Participant;
import com.project.muttley.repository.EventParticipationRepository;
import com.project.muttley.repository.MedalRepository;
import com.project.muttley.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserPortalService {

    private final ParticipantRepository participantRepository;
    private final EventParticipationRepository participationRepository;
    private final MedalRepository medalRepository;
    private final ParticipantMapper participantMapper;
    private final EventParticipationMapper participationMapper;
    private final MedalMapper medalMapper;

    public UserPortalService(
            ParticipantRepository participantRepository,
            EventParticipationRepository participationRepository,
            MedalRepository medalRepository,
            ParticipantMapper participantMapper,
            EventParticipationMapper participationMapper,
            MedalMapper medalMapper) {
        this.participantRepository = participantRepository;
        this.participationRepository = participationRepository;
        this.medalRepository = medalRepository;
        this.participantMapper = participantMapper;
        this.participationMapper = participationMapper;
        this.medalMapper = medalMapper;
    }

    @Transactional(readOnly = true)
    public ParticipantDashboardDTO getDashboard(String email) {
        Participant participant = participantRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Participante não encontrado para o usuário logado."));

        List<EventParticipation> participations = participationRepository.findByParticipant(participant);
        List<Medal> medals = new ArrayList<>();

        for (EventParticipation participation : participations) {
            medals.addAll(medalRepository.findByEventAndTargetRoleAndActiveTrue(
                    participation.getEvent(),
                    participation.getRoleInEvent()
            ));
        }

        ParticipantResponseDTO participantResponse = participantMapper.toResponse(participant);
        List<EventParticipationResponseDTO> participationResponses = participations.stream()
                .map(participationMapper::toResponse)
                .toList();
        List<MedalResponseDTO> medalResponses = medals.stream()
                .distinct()
                .map(medalMapper::toResponse)
                .toList();

        return new ParticipantDashboardDTO(participantResponse, participationResponses, medalResponses);
    }
}

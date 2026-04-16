package com.project.muttley.service;

import com.project.muttley.dto.participation.EventParticipationRequestDTO;
import com.project.muttley.dto.participation.EventParticipationResponseDTO;
import com.project.muttley.mapper.EventParticipationMapper;
import com.project.muttley.model.Event;
import com.project.muttley.model.EventParticipation;
import com.project.muttley.model.Participant;
import com.project.muttley.repository.EventParticipationRepository;
import com.project.muttley.repository.EventRepository;
import com.project.muttley.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventParticipationService {
    private final EventParticipationRepository participationRepository;
    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final EventParticipationMapper participationMapper;

    public EventParticipationService(
            EventParticipationRepository participationRepository,
            EventRepository eventRepository,
            ParticipantRepository participantRepository,
            EventParticipationMapper participationMapper) {
        this.participationRepository = participationRepository;
        this.eventRepository = eventRepository;
        this.participantRepository = participantRepository;
        this.participationMapper = participationMapper;
    }

    @Transactional
    public EventParticipationResponseDTO create(EventParticipationRequestDTO request) {
        EventParticipation participation = new EventParticipation();
        populateReferences(participation, request);
        participationMapper.updateEntity(participation, request);
        return participationMapper.toResponse(participationRepository.save(participation));
    }

    @Transactional(readOnly = true)
    public List<EventParticipationResponseDTO> findAll() {
        return participationRepository.findAll().stream().map(participationMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EventParticipationResponseDTO findById(Long id) {
        return participationMapper.toResponse(findEntity(id));
    }

    @Transactional
    public EventParticipationResponseDTO update(Long id, EventParticipationRequestDTO request) {
        EventParticipation participation = findEntity(id);
        populateReferences(participation, request);
        participationMapper.updateEntity(participation, request);
        return participationMapper.toResponse(participationRepository.save(participation));
    }

    @Transactional
    public void delete(Long id) {
        participationRepository.delete(findEntity(id));
    }

    private void populateReferences(EventParticipation participation, EventParticipationRequestDTO request) {
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado: " + request.eventId()));
        Participant participant = participantRepository.findById(request.participantId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Participante não encontrado: " + request.participantId())
                );
        participation.setEvent(event);
        participation.setParticipant(participant);
    }

    private EventParticipation findEntity(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participação no evento não encontrada: " + id));
    }
}

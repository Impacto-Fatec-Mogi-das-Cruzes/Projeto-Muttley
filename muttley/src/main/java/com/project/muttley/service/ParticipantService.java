package com.project.muttley.service;

import com.project.muttley.dto.participant.ParticipantRequestDTO;
import com.project.muttley.dto.participant.ParticipantResponseDTO;
import com.project.muttley.mapper.ParticipantMapper;
import com.project.muttley.model.Participant;
import com.project.muttley.repository.ParticipantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    public ParticipantService(ParticipantRepository participantRepository, ParticipantMapper participantMapper) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
    }

    @Transactional
    public ParticipantResponseDTO create(ParticipantRequestDTO request) {
        Participant participant = new Participant();
        participantMapper.updateEntity(participant, request);
        return participantMapper.toResponse(participantRepository.save(participant));
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponseDTO> findAll() {
        return participantRepository.findAll().stream().map(participantMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ParticipantResponseDTO findById(Long id) {
        return participantMapper.toResponse(findEntity(id));
    }

    @Transactional
    public ParticipantResponseDTO update(Long id, ParticipantRequestDTO request) {
        Participant participant = findEntity(id);
        participantMapper.updateEntity(participant, request);
        return participantMapper.toResponse(participantRepository.save(participant));
    }

    @Transactional
    public void delete(Long id) {
        participantRepository.delete(findEntity(id));
    }

    private Participant findEntity(Long id) {
        return participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Participante não encontrado: " + id));
    }
}

package com.project.muttley.service;

import com.project.muttley.dto.medal.MedalRequestDTO;
import com.project.muttley.dto.medal.MedalResponseDTO;
import com.project.muttley.mapper.MedalMapper;
import com.project.muttley.model.Event;
import com.project.muttley.model.Medal;
import com.project.muttley.repository.EventRepository;
import com.project.muttley.repository.MedalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MedalService {
    private final MedalRepository medalRepository;
    private final EventRepository eventRepository;
    private final MedalMapper medalMapper;

    public MedalService(MedalRepository medalRepository, EventRepository eventRepository, MedalMapper medalMapper) {
        this.medalRepository = medalRepository;
        this.eventRepository = eventRepository;
        this.medalMapper = medalMapper;
    }

    @Transactional
    public MedalResponseDTO create(MedalRequestDTO request) {
        Event event = findEvent(request.eventId());
        Medal medal = new Medal();
        medalMapper.updateEntity(medal, request);
        medal.setEvent(event);
        return medalMapper.toResponse(medalRepository.save(medal));
    }

    @Transactional(readOnly = true)
    public List<MedalResponseDTO> findAll() {
        return medalRepository.findAll().stream().map(medalMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MedalResponseDTO findById(Long id) {
        return medalMapper.toResponse(findEntity(id));
    }

    @Transactional
    public MedalResponseDTO update(Long id, MedalRequestDTO request) {
        Medal medal = findEntity(id);
        Event event = findEvent(request.eventId());
        medalMapper.updateEntity(medal, request);
        medal.setEvent(event);
        return medalMapper.toResponse(medalRepository.save(medal));
    }

    @Transactional
    public void delete(Long id) {
        medalRepository.delete(findEntity(id));
    }

    private Medal findEntity(Long id) {
        return medalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medalha não encontrada: " + id));
    }

    private Event findEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado: " + id));
    }
}

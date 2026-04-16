package com.project.muttley.service;

import com.project.muttley.dto.event.EventRequestDTO;
import com.project.muttley.dto.event.EventResponseDTO;
import com.project.muttley.mapper.EventMapper;
import com.project.muttley.model.Event;
import com.project.muttley.model.User;
import com.project.muttley.repository.EventRepository;
import com.project.muttley.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, UserRepository userRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventMapper = eventMapper;
    }

    @Transactional
    public EventResponseDTO create(EventRequestDTO request) {
        User createdBy = findUser(request.createdByUserId());
        Event event = new Event();
        eventMapper.updateEntity(event, request);
        event.setCreatedBy(createdBy);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> findAll() {
        return eventRepository.findAll().stream().map(eventMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EventResponseDTO findById(Long id) {
        return eventMapper.toResponse(findEntity(id));
    }

    @Transactional
    public EventResponseDTO update(Long id, EventRequestDTO request) {
        Event event = findEntity(id);
        User createdBy = findUser(request.createdByUserId());
        eventMapper.updateEntity(event, request);
        event.setCreatedBy(createdBy);
        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Transactional
    public void delete(Long id) {
        Event event = findEntity(id);
        eventRepository.delete(event);
    }

    private Event findEntity(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado: " + id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));
    }
}

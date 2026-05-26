package com.project.muttley.services.event;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.muttley.domain.event.dto.EventModalityItemDTO;
import com.project.muttley.domain.event.eventmodality.EventModality;
import com.project.muttley.repositories.EventModalityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventModalityService {

  private final EventModalityRepository eventModalityRepository;

  public List<EventModalityItemDTO> findAll() {
    return eventModalityRepository.findAllByOrderByNameAsc().stream()
        .map(this::toItem)
        .toList();
  }

  private EventModalityItemDTO toItem(EventModality modality) {
    return new EventModalityItemDTO(modality.getId(), modality.getName());
  }
}

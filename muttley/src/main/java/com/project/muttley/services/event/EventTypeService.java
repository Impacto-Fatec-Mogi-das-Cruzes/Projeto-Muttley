package com.project.muttley.services.event;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.muttley.domain.event.dto.EventTypeItemDTO;
import com.project.muttley.domain.event.eventtype.EventType;
import com.project.muttley.repositories.EventTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventTypeService {

  private final EventTypeRepository eventTypeRepository;

  public List<EventTypeItemDTO> findAll() {
    return eventTypeRepository.findAllByOrderByNameAsc().stream()
        .map(this::toItem)
        .toList();
  }

  private EventTypeItemDTO toItem(EventType type) {
    return new EventTypeItemDTO(type.getId(), type.getName());
  }
}

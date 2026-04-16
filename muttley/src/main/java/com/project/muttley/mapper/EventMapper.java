package com.project.muttley.mapper;

import com.project.muttley.dto.event.EventRequestDTO;
import com.project.muttley.dto.event.EventResponseDTO;
import com.project.muttley.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public void updateEntity(Event event, EventRequestDTO request) {
        event.setName(request.name());
        event.setDescription(request.description());
        event.setDate(request.date());
        event.setStartTime(request.startTime());
        event.setEndTime(request.endTime());
        event.setLocation(request.location());
        event.setResourcesNeeded(request.resourcesNeeded());
        event.setStatus(request.status());
    }

    public EventResponseDTO toResponse(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getDate(),
                event.getStartTime(),
                event.getEndTime(),
                event.getLocation(),
                event.getResourcesNeeded(),
                event.getStatus(),
                event.getCreatedBy().getId(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}

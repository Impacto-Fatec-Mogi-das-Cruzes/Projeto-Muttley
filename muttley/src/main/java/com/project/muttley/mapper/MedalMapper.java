package com.project.muttley.mapper;

import com.project.muttley.dto.medal.MedalRequestDTO;
import com.project.muttley.dto.medal.MedalResponseDTO;
import com.project.muttley.model.Medal;
import org.springframework.stereotype.Component;

@Component
public class MedalMapper {

    public void updateEntity(Medal medal, MedalRequestDTO request) {
        medal.setName(request.name());
        medal.setDescription(request.description());
        medal.setCategory(request.category());
        medal.setScore(request.score());
        medal.setTargetRole(request.targetRole());
        medal.setActive(request.active());
    }

    public MedalResponseDTO toResponse(Medal medal) {
        return new MedalResponseDTO(
                medal.getId(),
                medal.getEvent().getId(),
                medal.getEvent().getName(),
                medal.getName(),
                medal.getDescription(),
                medal.getCategory(),
                medal.getScore(),
                medal.getTargetRole(),
                medal.getActive(),
                medal.getCreatedAt(),
                medal.getUpdatedAt()
        );
    }
}

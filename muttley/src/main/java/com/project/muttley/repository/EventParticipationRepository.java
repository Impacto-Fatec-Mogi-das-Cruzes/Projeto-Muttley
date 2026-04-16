package com.project.muttley.repository;

import com.project.muttley.model.EventParticipation;
import com.project.muttley.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    List<EventParticipation> findByParticipant(Participant participant);
}

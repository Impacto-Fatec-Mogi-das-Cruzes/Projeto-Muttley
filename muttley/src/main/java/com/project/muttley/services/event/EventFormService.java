package com.project.muttley.services.event;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.project.muttley.domain.event.Event;
import com.project.muttley.domain.event.dto.EventFormRequestDTO;
import com.project.muttley.domain.event.dto.EventFormResponseDTO;
import com.project.muttley.domain.event.dto.EventPublicInfoDTO;
import com.project.muttley.domain.event.eventparticipant.EventParticipant;
import com.project.muttley.domain.event.mapper.EventMapper;
import com.project.muttley.domain.participant.Participant;
import com.project.muttley.domain.participant.participanttype.ParticipantType;
import com.project.muttley.exceptions.AlreadyRegisteredInEventException;
import com.project.muttley.exceptions.EventNotFoundException;
import com.project.muttley.exceptions.EventRegistrationException;
import com.project.muttley.exceptions.InvalidCpfException;
import com.project.muttley.repositories.EventParticipantRepository;
import com.project.muttley.repositories.EventRepository;
import com.project.muttley.repositories.ParticipantRepository;
import com.project.muttley.repositories.ParticipantTypeRepository;
import com.project.muttley.util.CpfUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventFormService {

  private final EventRepository eventRepository;
  private final EventParticipantRepository eventParticipantRepository;
  private final ParticipantRepository participantRepository;
  private final ParticipantTypeRepository participantTypeRepository;
  private final EventMapper eventMapper;

  public EventPublicInfoDTO getPublicInfo(java.util.UUID eventId) {
    Event event = findEvent(eventId);
    return eventMapper.toPublicInfo(
        event,
        isRegistrationOpen(event),
        isPresenceOpen(event));
  }

  @Transactional
  public EventFormResponseDTO register(java.util.UUID eventId, EventFormRequestDTO request) {
    Event event = findEvent(eventId);
    validateRegistrationWindow(event);

    String cpf = requireValidCpf(request.cpf());
    Participant participant = findOrCreateParticipant(request.name(), request.email(), cpf);

    if (eventParticipantRepository.existsByEvent_IdAndParticipant_Id(eventId, participant.getId())) {
      throw new AlreadyRegisteredInEventException("Participante já está inscrito neste evento");
    }

    validateCapacity(event);

    EventParticipant registration = new EventParticipant();
    registration.setEvent(event);
    registration.setParticipant(participant);
    registration.setParticipantType(findParticipantType(EventMapper.ROLE_LISTENER));
    registration.setRegisteredAt(LocalDateTime.now());
    registration.setPresent(false);

    return eventMapper.toRegistrationResponse(eventParticipantRepository.save(registration));
  }

  @Transactional
  public EventFormResponseDTO confirmPresence(java.util.UUID eventId, EventFormRequestDTO request) {
    Event event = findEvent(eventId);
    validatePresenceWindow(event);

    String cpf = requireValidCpf(request.cpf());
    Participant participant = findOrCreateParticipant(request.name(), request.email(), cpf);

    EventParticipant registration = eventParticipantRepository
        .findByEvent_IdAndParticipant_Cpf(eventId, cpf)
        .orElseGet(() -> createLateRegistration(event, participant));

    if (Boolean.TRUE.equals(registration.getPresent())) {
      throw new AlreadyRegisteredInEventException("Presença já confirmada neste evento");
    }

    registration.setPresent(true);
    registration.setCheckInAt(LocalDateTime.now());

    return eventMapper.toPresenceResponse(eventParticipantRepository.save(registration));
  }

  private EventParticipant createLateRegistration(Event event, Participant participant) {
    validateCapacity(event);

    EventParticipant registration = new EventParticipant();
    registration.setEvent(event);
    registration.setParticipant(participant);
    registration.setParticipantType(findParticipantType(EventMapper.ROLE_LISTENER));
    registration.setRegisteredAt(LocalDateTime.now());
    registration.setPresent(false);
    return eventParticipantRepository.save(registration);
  }

  private Participant findOrCreateParticipant(String name, String email, String cpf) {
    return participantRepository.findByCpf(cpf)
        .map(existing -> updateParticipantIfNeeded(existing, name, email))
        .orElseGet(() -> createParticipant(name, email, cpf));
  }

  private Participant updateParticipantIfNeeded(Participant participant, String name, String email) {
    participant.setName(name);
    participant.setEmail(email);
    return participantRepository.save(participant);
  }

  private Participant createParticipant(String name, String email, String cpf) {
    Participant participant = new Participant();
    participant.setName(name);
    participant.setEmail(email);
    participant.setCpf(cpf);
    participant.setPoints(0);
    return participantRepository.save(participant);
  }

  private void validateCapacity(Event event) {
    if (event.getCapacity() == null) {
      return;
    }
    long registered = eventParticipantRepository.countByEvent_Id(event.getId());
    if (registered >= event.getCapacity()) {
      throw new EventRegistrationException("O evento atingiu a capacidade máxima");
    }
  }

  private void validateRegistrationWindow(Event event) {
    if (isFinalized(event)) {
      throw new EventRegistrationException("Inscrição indisponível para eventos finalizados");
    }
    if (!isInProgress(event)) {
      throw new EventRegistrationException("As inscrições não estão abertas para este evento");
    }
    if (!LocalDateTime.now().isBefore(getEventStartDateTime(event))) {
      throw new EventRegistrationException(
          "A inscrição só é permitida antes do início do evento");
    }
  }

  private void validatePresenceWindow(Event event) {
    if (isFinalized(event)) {
      throw new EventRegistrationException("Confirmação de presença indisponível para eventos finalizados");
    }
    if (!isInProgress(event)) {
      throw new EventRegistrationException("A confirmação de presença não está aberta para este evento");
    }

    LocalDateTime now = LocalDateTime.now();

    if (now.isBefore(getEventStartDateTime(event))
        || now.isAfter(getEventEndDateTime(event))) {

      throw new EventRegistrationException(
          "A confirmação de presença só é permitida durante o período do evento");
    }
  }

  private LocalDateTime getEventStartDateTime(Event event) {
    return LocalDateTime.of(
        event.getDateStart(),
        event.getHourStart());
  }

  private LocalDateTime getEventEndDateTime(Event event) {
    return LocalDateTime.of(
        event.getDateEnd() != null ? event.getDateEnd() : event.getDateStart(),
        event.getHourEnd() != null ? event.getHourEnd() : event.getHourStart());
  }

  private boolean isRegistrationOpen(Event event) {
    return isInProgress(event)
        && !isFinalized(event)
        && LocalDateTime.now().isBefore(getEventStartDateTime(event));
  }

  private boolean isPresenceOpen(Event event) {
    LocalDateTime now = LocalDateTime.now();

    return isInProgress(event)
        && !isFinalized(event)
        && !now.isBefore(getEventStartDateTime(event))
        && !now.isAfter(getEventEndDateTime(event));
  }

  private boolean isFinalized(Event event) {
    return event.getEventStatus() != null
        && EventMapper.STATUS_FINALIZED.equalsIgnoreCase(event.getEventStatus().getName());
  }

  private boolean isInProgress(Event event) {
    return event.getEventStatus() != null
        && EventMapper.STATUS_IN_PROGRESS.equalsIgnoreCase(event.getEventStatus().getName());
  }

  private String requireValidCpf(String cpf) {
    if (!CpfUtils.isValid(cpf)) {
      throw new InvalidCpfException("CPF inválido");
    }
    return CpfUtils.normalize(cpf);
  }

  private ParticipantType findParticipantType(String roleName) {
    return participantTypeRepository.findByNameIgnoreCase(roleName)
        .orElseThrow(() -> new EventRegistrationException("Tipo de participante não configurado: " + roleName));
  }

  private Event findEvent(java.util.UUID eventId) {
    return eventRepository.findById(eventId)
        .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));
  }
}

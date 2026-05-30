package com.project.muttley.domain.event.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.project.muttley.configuration.AppProperties;
import com.project.muttley.domain.event.Event;
import com.project.muttley.domain.event.EventKeywords;
import com.project.muttley.domain.event.dto.EventDetailDTO;
import com.project.muttley.domain.event.dto.EventFormResponseDTO;
import com.project.muttley.domain.event.dto.EventParticipantItemDTO;
import com.project.muttley.domain.event.dto.EventPublicInfoDTO;
import com.project.muttley.domain.event.dto.EventRequestDTO;
import com.project.muttley.domain.event.dto.EventResponseDTO;
import com.project.muttley.domain.event.dto.EventStaffDTO;
import com.project.muttley.domain.event.dto.EventSummaryDTO;
import com.project.muttley.domain.event.eventmodality.EventModality;
import com.project.muttley.domain.event.eventparticipant.EventParticipant;
import com.project.muttley.domain.event.eventtype.EventType;
import com.project.muttley.util.CpfUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventMapper {

  public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
  public static final String STATUS_FINALIZED = "FINALIZED";

  public static final String ROLE_LISTENER = "LISTENER";
  public static final String ROLE_ORGANIZER = "ORGANIZER";
  public static final String ROLE_SPEAKER = "SPEAKER";
  public static final String ROLE_SPONSOR = "SPONSOR";

  private final AppProperties appProperties;

  public void applyRequest(Event event, EventRequestDTO dto, EventModality modality, EventType eventType) {
    event.setTitle(dto.title());
    event.setDateStart(dto.startDate());
    event.setDateEnd(dto.endDate());
    event.setWorkLoad(dto.workload());
    event.setPoints(dto.points());
    event.setModality(modality);
    event.setEventType(eventType);
    event.setCapacity(dto.capacity());
    event.setSubject(dto.subject());
    event.setDescription(dto.description());
    event.setAddressOrLink(dto.addressOrLink());
    event.setKeywords(EventKeywords.normalize(dto.keywords()));
    event.setNameSignature(dto.nameSignature());
    event.setPositionSignature(dto.positionSignature());
  }

  public EventResponseDTO toResponse(Event event) {
    return new EventResponseDTO(
        event.getId(),
        event.getTitle(),
        event.getDateStart(),
        event.getDateEnd(),
        event.getWorkLoad(),
        event.getPoints(),
        refId(event.getEventType()),
        refName(event.getEventType()),
        refId(event.getModality()),
        refName(event.getModality()),
        event.getSubject(),
        event.getKeywords(),
        event.getDescription(),
        event.getCapacity(),
        event.getAddressOrLink(),
        event.getImageBackgroundUrl(),
        event.getImageSignatureUrl(),
        event.getNameSignature(),
        event.getPositionSignature(),
        refId(event.getEventStatus()),
        refName(event.getEventStatus()),
        buildRegistrationUrl(event.getId()),
        buildPresenceUrl(event.getId()),
        event.getImageQrCodeInscriptionUrl(),
        event.getImageQrCodePresenceUrl(),
        event.getCreatedAt(),
        event.getUpdatedAt());
  }

  public EventDetailDTO toDetail(
      Event event,
      List<EventStaffDTO> organizers,
      List<EventStaffDTO> speakers,
      List<EventStaffDTO> sponsors,
      long registeredCount,
      long presentCount,
      Page<EventParticipantItemDTO> participants) {
    return new EventDetailDTO(
        event.getId(),
        event.getTitle(),
        event.getDateStart(),
        event.getDateEnd(),
        event.getWorkLoad(),
        event.getPoints(),
        refId(event.getEventType()),
        refName(event.getEventType()),
        event.getSubject(),
        event.getKeywords(),
        event.getDescription(),
        refId(event.getModality()),
        refName(event.getModality()),
        event.getAddressOrLink(),
        event.getCapacity(),
        organizers,
        speakers,
        sponsors,
        event.getImageBackgroundUrl(),
        event.getImageSignatureUrl(),
        event.getNameSignature(),
        event.getPositionSignature(),
        refId(event.getEventStatus()),
        refName(event.getEventStatus()),
        buildRegistrationUrl(event.getId()),
        buildPresenceUrl(event.getId()),
        event.getImageQrCodeInscriptionUrl(),
        event.getImageQrCodePresenceUrl(),
        registeredCount,
        presentCount,
        participants,
        event.getCreatedAt(),
        event.getUpdatedAt());
  }

  public EventSummaryDTO toSummary(Event event) {
    return new EventSummaryDTO(
        event.getId(),
        event.getTitle(),
        event.getDateStart(),
        event.getDateEnd(),
        refName(event.getEventStatus()),
        refName(event.getEventType()),
        refName(event.getModality()),
        event.getPoints());
  }

  public EventPublicInfoDTO toPublicInfo(Event event, boolean registrationOpen, boolean presenceOpen) {
    return new EventPublicInfoDTO(
        event.getId(),
        event.getTitle(),
        event.getDateStart(),
        event.getDateEnd(),
        refName(event.getEventStatus()),
        registrationOpen,
        presenceOpen);
  }

  public EventParticipantItemDTO toParticipantItem(EventParticipant registration) {
    boolean present = Boolean.TRUE.equals(registration.getPresent());
    String status = present ? "PRESENTE" : "INSCRITO";

    return new EventParticipantItemDTO(
        registration.getId(),
        registration.getParticipant().getId(),
        registration.getParticipant().getName(),
        CpfUtils.format(registration.getParticipant().getCpf()),
        registration.getParticipant().getEmail(),
        registration.getParticipantType() != null ? registration.getParticipantType().getName() : ROLE_LISTENER,
        registration.getRegisteredAt(),
        registration.getCheckInAt(),
        present,
        status);
  }

  public EventStaffDTO toStaff(EventParticipant registration) {
    return new EventStaffDTO(
        registration.getParticipant().getId(),
        registration.getParticipant().getName(),
        CpfUtils.format(registration.getParticipant().getCpf()),
        registration.getParticipant().getEmail(),
        registration.getParticipantType().getName());
  }

  public EventFormResponseDTO toRegistrationResponse(EventParticipant registration) {
    return new EventFormResponseDTO(
        registration.getEvent().getId(),
        registration.getParticipant().getId(),
        registration.getParticipant().getName(),
        CpfUtils.format(registration.getParticipant().getCpf()),
        "Inscrição realizada com sucesso",
        registration.getRegisteredAt(),
        registration.getCheckInAt(),
        Boolean.TRUE.equals(registration.getPresent()));
  }

  public EventFormResponseDTO toPresenceResponse(EventParticipant registration) {
    return new EventFormResponseDTO(
        registration.getEvent().getId(),
        registration.getParticipant().getId(),
        registration.getParticipant().getName(),
        CpfUtils.format(registration.getParticipant().getCpf()),
        "Presença confirmada com sucesso",
        registration.getRegisteredAt(),
        registration.getCheckInAt(),
        Boolean.TRUE.equals(registration.getPresent()));
  }

  public static UUID parseUuid(String value, String fieldName) {
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("UUID inválido para o campo: " + fieldName);
    }
  }

  public String buildRegistrationUrl(UUID eventId) {
    return trimBaseUrl() + "/events/" + eventId + "/registration";
  }

  public String buildPresenceUrl(UUID eventId) {
    return trimBaseUrl() + "/events/" + eventId + "/presence";
  }

  private String trimBaseUrl() {
    String base = appProperties.publicBaseUrl();
    if (base == null || base.isBlank()) {
      return "http://localhost:3000";
    }
    return base.replaceAll("/$", "");
  }

  private UUID refId(Object entity) {
    if (entity == null) {
      return null;
    }
    try {
      return (UUID) entity.getClass().getMethod("getId").invoke(entity);
    } catch (ReflectiveOperationException ex) {
      return null;
    }
  }

  private String refName(Object entity) {
    if (entity == null) {
      return null;
    }
    try {
      return (String) entity.getClass().getMethod("getName").invoke(entity);
    } catch (ReflectiveOperationException ex) {
      return null;
    }
  }
}

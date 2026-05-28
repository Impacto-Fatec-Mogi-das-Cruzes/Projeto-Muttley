package com.project.muttley.services.event;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import com.project.muttley.domain.certificate.Certificate;
import com.project.muttley.domain.certificate.dto.CertificateGenerateDTO;
import com.project.muttley.domain.certificate.dto.CertificateRequestDTO;
import com.project.muttley.domain.event.Event;
import com.project.muttley.domain.event.EventKeywords;
import com.project.muttley.domain.event.dto.EventDetailDTO;
import com.project.muttley.domain.event.dto.EventParticipantDetailDTO;
import com.project.muttley.domain.event.dto.EventParticipantItemDTO;
import com.project.muttley.domain.event.dto.EventRequestDTO;
import com.project.muttley.domain.event.dto.EventResponseDTO;
import com.project.muttley.domain.event.dto.EventStaffDTO;
import com.project.muttley.domain.event.dto.EventSummaryDTO;
import com.project.muttley.domain.event.eventmodality.EventModality;
import com.project.muttley.domain.event.eventparticipant.EventParticipant;
import com.project.muttley.domain.event.eventstatus.EventStatus;
import com.project.muttley.domain.event.eventtype.EventType;
import com.project.muttley.domain.event.mapper.EventMapper;
import com.project.muttley.domain.participant.Participant;
import com.project.muttley.domain.participant.participanttype.ParticipantType;
import com.project.muttley.exceptions.EventNotFoundException;
import com.project.muttley.exceptions.ResourceNotFoundException;
import com.project.muttley.repositories.EventModalityRepository;
import com.project.muttley.repositories.EventParticipantRepository;
import com.project.muttley.repositories.EventRepository;
import com.project.muttley.repositories.EventStatusRepository;
import com.project.muttley.repositories.EventTypeRepository;
import com.project.muttley.repositories.ParticipantRepository;
import com.project.muttley.repositories.ParticipantTypeRepository;
import com.project.muttley.services.certificate.CertificateService;
import com.project.muttley.services.email.EmailService;
import com.project.muttley.services.participant.ParticipantService;
import com.project.muttley.services.storage.QrCodeService;
import com.project.muttley.services.storage.S3StorageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventService {

  private final EventRepository eventRepository;
  private final EventTypeRepository eventTypeRepository;
  private final EventModalityRepository eventModalityRepository;
  private final EventStatusRepository eventStatusRepository;
  private final EventParticipantRepository eventParticipantRepository;
  private final ParticipantRepository participantRepository;
  private final ParticipantTypeRepository participantTypeRepository;
  private final S3StorageService s3StorageService;
  private final QrCodeService qrCodeService;
  private final EventMapper eventMapper;

  @Autowired
  private RestClient pdfRestClient;

  @Autowired
  private CertificateService certificateService;

  @Autowired
  private ParticipantService participantService;

  @Autowired
  private EmailService emailService;

  @Value("${spring.mail.from}")
  private String emailFrom;

  @Value("${app.public-base-url}")
  private String frontUrl;

  @Transactional
  public EventResponseDTO create(EventRequestDTO request, MultipartFile signature, MultipartFile background) {
    requireSignature(signature);
    validarRegrasEvento(request);

    EventType eventType = findEventType(request.typeId());
    EventModality modality = findEventModality(request.modalityId());
    EventStatus status = findEventStatus(EventMapper.STATUS_IN_PROGRESS);

    Event event = new Event();
    eventMapper.applyRequest(event, request, modality, eventType);
    ajustarDataFim(event);
    event.setEventStatus(status);

    Event saved = eventRepository.save(event);

    applySignatureUpload(saved, signature, true);
    applyBackgroundUpload(saved, background, true);

    gerarEArmazenarQRCodes(saved);
    syncStaff(saved, request);

    return eventMapper.toResponse(eventRepository.save(saved));
  }

  public Page<EventSummaryDTO> findAll(String title, Pageable pageable) {
    return eventRepository
        .findAllFiltered(title, pageable)
        .map(eventMapper::toSummary);
  }

  public Page<EventParticipantDetailDTO> getEventParticipant(
      UUID participantId,
      String search,
      Pageable pageable) {

    Page<Event> events;

    if (search == null || search.isBlank()) {
      events = eventRepository
          .findByEventParticipantsParticipantId(participantId, pageable);
    } else {
      events = eventRepository
          .findByTitleContainingIgnoreCaseAndEventParticipantsParticipantId(
              search,
              participantId,
              pageable);
    }

    return events.map(e -> new EventParticipantDetailDTO(
        e.getId(),
        e.getTitle(),
        e.getModality(),
        e.getDateStart(),
        e.getDateEnd() != null
            ? e.getDateEnd().toString()
            : "Não definido",
        e.getEventStatus()));
  }

  public EventResponseDTO findById(UUID id) {
    return eventMapper.toResponse(findEventById(id));
  }

  public Event getById(UUID id) {
    return eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Evento não encontrado: " + id));
  }

  public EventDetailDTO findDetails(UUID id, String role, Pageable pageable) {
    Event event = findEventById(id);
    String normalizedRole = normalizeRole(role);

    List<EventStaffDTO> organizers = loadStaff(event.getId(), EventMapper.ROLE_ORGANIZER);
    List<EventStaffDTO> speakers = loadStaff(event.getId(), EventMapper.ROLE_SPEAKER);
    List<EventStaffDTO> sponsors = loadStaff(event.getId(), EventMapper.ROLE_SPONSOR);

    long registeredCount = eventParticipantRepository.countByEvent_Id(id);
    long presentCount = eventParticipantRepository.countByEvent_IdAndPresentTrue(id);

    Page<EventParticipantItemDTO> participants = eventParticipantRepository
        .findByEventWithFilters(id, normalizedRole, pageable)
        .map(eventMapper::toParticipantItem);

    return eventMapper.toDetail(
        event,
        organizers,
        speakers,
        sponsors,
        registeredCount,
        presentCount,
        participants);
  }

  @Transactional
  public EventResponseDTO update(UUID id, EventRequestDTO request, MultipartFile signature, MultipartFile background) {
    validarRegrasEvento(request);
    Event event = findEventById(id);
    ensureNotFinalized(event);

    EventType eventType = findEventType(request.typeId());
    EventModality modality = findEventModality(request.modalityId());

    eventMapper.applyRequest(event, request, modality, eventType);
    ajustarDataFim(event);
    applySignatureUpload(event, signature, false);
    applyBackgroundUpload(event, background, false);
    if (event.getImageQrCodeInscriptionUrl() == null || event.getImageQrCodePresenceUrl() == null) {
      gerarEArmazenarQRCodes(event);
    }
    syncStaff(event, request);

    return eventMapper.toResponse(eventRepository.save(event));
  }

  @Transactional
  public void delete(UUID id) {
    Event event = findEventById(id);
    eventParticipantRepository.deleteByEvent_Id(event.getId());
    s3StorageService.deleteByUrl(event.getImageSignatureUrl());
    s3StorageService.deleteByUrl(event.getImageQrCodeInscriptionUrl());
    s3StorageService.deleteByUrl(event.getImageQrCodePresenceUrl());
    eventRepository.delete(event);
  }

  @Transactional
  public EventResponseDTO finalizeEvent(UUID id) {
    Event event = findEventById(id);
    EventStatus finalizedStatus = findEventStatus(EventMapper.STATUS_FINALIZED);

    if (event.getEventStatus() != null
        && EventMapper.STATUS_FINALIZED.equalsIgnoreCase(event.getEventStatus().getName())) {
      throw new IllegalStateException("O evento já está finalizado");
    }

    event.setEventStatus(finalizedStatus);

    List<Participant> participants = event.getEventParticipants()
        .stream()
        .filter(ep -> Boolean.TRUE.equals(ep.getPresent()))
        .map(EventParticipant::getParticipant)
        .toList();

    for (Participant participant : participants) {
      CertificateRequestDTO certificateDTO = new CertificateRequestDTO(participant.getId(), event.getId());

      // TODO verify if really need to add points for every participant
      Certificate newCertificate = certificateService.create(certificateDTO);

      participantService.addPoints(participant.getId(), event.getPoints());

      String certificateQrUrl = frontUrl + "/certificate/" + newCertificate.getId();

      CertificateGenerateDTO dto = new CertificateGenerateDTO(participant.getName(), event.getSubject(),
          event.getTitle(), event.getDateStart().toString(), event.getWorkLoad(), event.getNameSignature(),
          event.getPositionSignature(), event.getImageBackgroundUrl(), event.getImageSignatureUrl(),
          newCertificate.getId().toString(), certificateQrUrl);

      byte[] pdf = pdfRestClient.post()
          .uri("/api/certificate/generate")
          .contentType(MediaType.APPLICATION_JSON)
          .body(dto)
          .retrieve()
          .body(byte[].class);

      String urlStorage = s3StorageService.uploadParticipantCertificate(
          pdf,
          participant.getId(),
          newCertificate.getId());

      certificateService.updateUrlCertificate(newCertificate.getId(), urlStorage);

      List<String> competencies = EventKeywords.parse(event.getKeywords());
      EventKeywords.validateMax(event.getKeywords(), 10);

      emailService.sendEmail(
          emailFrom,
          participant.getEmail(),
          "Seu certificado do evento " + event.getTitle() + "está disponível",
          emailService.buildEmailHtmlParticipantion(event.getTitle(), competencies),
          pdf);
    }

    return eventMapper.toResponse(eventRepository.save(event));
  }

  private void syncStaff(Event event, EventRequestDTO request) {
    List<String> staffRoles = List.of(
        EventMapper.ROLE_ORGANIZER,
        EventMapper.ROLE_SPEAKER,
        EventMapper.ROLE_SPONSOR);

    eventParticipantRepository.deleteByEvent_IdAndParticipantType_NameIn(event.getId(), staffRoles);

    assignStaff(event, request.organizerIds(), EventMapper.ROLE_ORGANIZER);
    assignStaff(event, request.speakerIds(), EventMapper.ROLE_SPEAKER);
    assignStaff(event, request.sponsorIds(), EventMapper.ROLE_SPONSOR);
  }

  private void assignStaff(Event event, List<UUID> participantIds, String roleName) {
    if (participantIds == null || participantIds.isEmpty()) {
      return;
    }

    ParticipantType role = participantTypeRepository.findByNameIgnoreCase(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Tipo de participante não encontrado: " + roleName));

    for (UUID participantId : participantIds) {
      Participant participant = participantRepository.findById(participantId)
          .orElseThrow(() -> new ResourceNotFoundException("Participante não encontrado: " + participantId));

      var existingRegistration = eventParticipantRepository
          .findByEvent_IdAndParticipant_Id(event.getId(), participantId);

      if (existingRegistration.isPresent()) {
        EventParticipant existing = existingRegistration.get();
        existing.setParticipantType(role);
        eventParticipantRepository.save(existing);
        continue;
      }

      EventParticipant registration = new EventParticipant();
      registration.setEvent(event);
      registration.setParticipant(participant);
      registration.setParticipantType(role);
      registration.setRegisteredAt(java.time.LocalDateTime.now());
      registration.setPresent(false);
      eventParticipantRepository.save(registration);
    }
  }

  private List<EventStaffDTO> loadStaff(UUID eventId, String roleName) {
    return eventParticipantRepository
        .findByEvent_IdAndParticipantType_NameIn(eventId, List.of(roleName))
        .stream()
        .map(eventMapper::toStaff)
        .toList();
  }

  private void ensureNotFinalized(Event event) {
    if (event.getEventStatus() != null
        && EventMapper.STATUS_FINALIZED.equalsIgnoreCase(event.getEventStatus().getName())) {
      throw new IllegalStateException("Eventos finalizados não podem ser editados");
    }
  }

  private void applySignatureUpload(Event event, MultipartFile signature, boolean required) {
    if (signature == null || signature.isEmpty()) {
      if (required) {
        throw new IllegalArgumentException("O arquivo de assinatura é obrigatório");
      }
      return;
    }

    String previousUrl = event.getImageSignatureUrl();
    String newUrl = s3StorageService.uploadEventSignature(signature, event.getId());
    event.setImageSignatureUrl(newUrl);

    if (previousUrl != null && !previousUrl.equals(newUrl)) {
      s3StorageService.deleteByUrl(previousUrl);
    }
  }

  private void applyBackgroundUpload(
      Event event,
      MultipartFile background,
      boolean required) {

    if (background == null || background.isEmpty()) {

      if (required) {
        throw new IllegalArgumentException(
            "O background do evento é obrigatório");
      }

      return;
    }

    String previousUrl = event.getImageBackgroundUrl();

    String newUrl = s3StorageService.uploadEventBackground(
        background,
        event.getId());

    event.setImageBackgroundUrl(newUrl);

    if (previousUrl != null && !previousUrl.equals(newUrl)) {
      s3StorageService.deleteByUrl(previousUrl);
    }
  }

  private void requireSignature(MultipartFile signature) {
    if (signature == null || signature.isEmpty()) {
      throw new IllegalArgumentException("O arquivo de assinatura é obrigatório");
    }
  }

  private void validarRegrasEvento(EventRequestDTO request) {
    if (request.startDate() == null) {
      throw new IllegalArgumentException("A data inicial é obrigatória");
    }
    if (request.endDate() != null && request.endDate().isBefore(request.startDate())) {
      throw new IllegalArgumentException("A data final não pode ser anterior à data inicial");
    }
    if (EventKeywords.parse(request.keywords()).isEmpty()) {
      throw new IllegalArgumentException("Informe pelo menos uma competência nas keywords");
    }
    if (request.organizerIds() == null || request.organizerIds().isEmpty()) {
      throw new IllegalArgumentException("É obrigatório informar ao menos um organizador");
    }
    EventKeywords.validateMax(request.keywords(), 10);
  }

  private void ajustarDataFim(Event event) {
    if (event.getDateEnd() == null) {
      event.setDateEnd(event.getDateStart());
    }
  }

  private void gerarEArmazenarQRCodes(Event event) {
    String registrationUrl = eventMapper.buildRegistrationUrl(event.getId());
    String presenceUrl = eventMapper.buildPresenceUrl(event.getId());

    String antigoInscricao = event.getImageQrCodeInscriptionUrl();
    String antigoPresenca = event.getImageQrCodePresenceUrl();

    byte[] registrationQr = qrCodeService.generatePng(registrationUrl);
    byte[] presenceQr = qrCodeService.generatePng(presenceUrl);

    String registrationQrUrl = s3StorageService.uploadEventQrCode(registrationQr, event.getId(), "registration");
    String presenceQrUrl = s3StorageService.uploadEventQrCode(presenceQr, event.getId(), "presence");

    event.setImageQrCodeInscriptionUrl(registrationQrUrl);
    event.setImageQrCodePresenceUrl(presenceQrUrl);

    if (antigoInscricao != null && !antigoInscricao.equals(registrationQrUrl)) {
      s3StorageService.deleteByUrl(antigoInscricao);
    }
    if (antigoPresenca != null && !antigoPresenca.equals(presenceQrUrl)) {
      s3StorageService.deleteByUrl(antigoPresenca);
    }
  }

  private Event findEventById(UUID id) {
    return eventRepository
        .findById(id)
        .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));
  }

  private EventType findEventType(String typeId) {
    UUID uuid = EventMapper.parseUuid(typeId, "typeId");
    return eventTypeRepository
        .findById(uuid)
        .orElseThrow(() -> new ResourceNotFoundException("Tipo de evento não encontrado"));
  }

  private EventModality findEventModality(String modalityId) {
    UUID uuid = EventMapper.parseUuid(modalityId, "modalityId");
    return eventModalityRepository
        .findById(uuid)
        .orElseThrow(() -> new ResourceNotFoundException("Modalidade de evento não encontrada"));
  }

  private EventStatus findEventStatus(String statusName) {
    return eventStatusRepository
        .findByNameIgnoreCase(statusName)
        .orElseThrow(() -> new ResourceNotFoundException("Status de evento não encontrado: " + statusName));
  }

  private String normalizeRole(String role) {
    if (role == null || role.isBlank()) {
      return null;
    }
    return switch (role.trim().toLowerCase()) {
      case "organizador", "organizer" -> EventMapper.ROLE_ORGANIZER;
      case "palestrante", "speaker" -> EventMapper.ROLE_SPEAKER;
      case "patrocinador", "sponsor" -> EventMapper.ROLE_SPONSOR;
      case "ouvinte", "listener" -> EventMapper.ROLE_LISTENER;
      default -> role.trim().toUpperCase();
    };
  }
}

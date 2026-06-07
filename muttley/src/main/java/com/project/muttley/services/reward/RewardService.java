package com.project.muttley.services.reward;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.project.muttley.domain.certificate.Certificate;
import com.project.muttley.domain.certificate.dto.CertificateGenerateCustomDTO;
import com.project.muttley.domain.certificate.dto.CertificateRequestDTO;
import com.project.muttley.domain.event.Event;
import com.project.muttley.domain.event.EventKeywords;
import com.project.muttley.domain.participant.Participant;
import com.project.muttley.services.certificate.CertificateService;
import com.project.muttley.services.email.EmailService;
import com.project.muttley.services.event.EventService;
import com.project.muttley.services.participant.ParticipantService;
import com.project.muttley.services.storage.S3StorageService;

import jakarta.transaction.Transactional;

@Service
public class RewardService {

  @Autowired
  private ParticipantService participantService;

  @Autowired
  private EventService eventService;

  @Autowired
  private CertificateService certificateService;

  @Autowired
  private S3StorageService s3StorageService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private RestClient pdfRestClient;

  @Value("${spring.mail.from}")
  private String emailFrom;

  @Value("${app.public-base-url}")
  private String frontUrl;

  @Transactional
  public String rewardParticipants(
      List<UUID> participantIds,
      String description,
      String keywords,
      UUID eventId) {

    List<String> competencies = EventKeywords.parse(keywords);
    EventKeywords.validateMax(keywords, 10);

    List<Participant> participants = participantService.getByIds(participantIds);

    Event event = eventService.getById(eventId);

    for (Participant participant : participants) {

      CertificateRequestDTO certificateDTO = new CertificateRequestDTO(participant.getId(), event.getId());

      participantService.addMedals(participant.getId(), 1);
      Certificate newCertificate = certificateService.create(certificateDTO);

      String certificateQrUrl = frontUrl + "/certificate/" + newCertificate.getId();

      // TODO probably here will change imagebackground from url to file
      CertificateGenerateCustomDTO dto = new CertificateGenerateCustomDTO(participant.getName(), description,
          event.getNameSignature(), event.getPositionSignature(), event.getImageBackgroundUrl(),
          event.getImageSignatureUrl(), newCertificate.getId().toString(), certificateQrUrl);

      byte[] pdf = pdfRestClient.post()
          .uri("/api/certificate/generate-custom")
          .contentType(MediaType.APPLICATION_JSON)
          .body(dto)
          .retrieve()
          .body(byte[].class);

      String urlStorage = s3StorageService.uploadParticipantCertificate(
          pdf,
          participant.getId(),
          newCertificate.getId());

      certificateService.updateUrlCertificate(newCertificate.getId(), urlStorage);

      emailService.sendEmail(
          emailFrom,
          participant.getEmail(),
          "Parabéns! Seu certificado especial do evento " + event.getTitle() + " está disponível",
          emailService.buildEmailHtml(event.getTitle(), competencies),
          pdf);
    }

    return "ok";
  }
}

package com.project.muttley.services.participant;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.muttley.domain.participant.Participant;
import com.project.muttley.domain.participant.dto.ParticipantRequestDTO;
import com.project.muttley.domain.participant.dto.ParticipantResponseDTO;
import com.project.muttley.exceptions.ParticipantAlreadyExistsException;
import com.project.muttley.repositories.ParticipantRepository;
import com.project.muttley.util.CpfUtils;

import jakarta.transaction.Transactional;

@Service
public class ParticipantService {

  @Autowired
  private ParticipantRepository participantRepository;

  @Transactional
  public Participant create(ParticipantRequestDTO dto) {

    String cpf = CpfUtils.normalize(dto.cpf());
    if (!CpfUtils.isValid(cpf)) {
      throw new IllegalArgumentException("CPF inválido");
    }

    if (participantRepository.existsByCpf(cpf)) {
      throw new ParticipantAlreadyExistsException("Participante já existente");
    }

    Participant newParticipant = new Participant();

    newParticipant.setCpf(cpf);
    newParticipant.setName(dto.name());
    newParticipant.setEmail(dto.email());
    newParticipant.setPoints(0);
    newParticipant.setMedals(0);

    participantRepository.save(newParticipant);

    return newParticipant;

  }

  public Page<ParticipantResponseDTO> get(
      int page,
      int size,
      String sortBy,
      String direction) {

    Sort sort = direction.equalsIgnoreCase("desc")
        ? Sort.by(sortBy).descending()
        : Sort.by(sortBy).ascending();

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Participant> participants = participantRepository.findAll(pageable);

    return participants.map(p -> new ParticipantResponseDTO(
        p.getId(),
        p.getName(),
        p.getCpf(),
        p.getEmail(),
        p.getPoints(),
        p.getCertificates().size(),
        // p.getMedals().size()));
        p.getMedals()));
  }

  public List<Participant> getByIds(List<UUID> ids) {
    List<Participant> participants = participantRepository.findAllByIdIn(ids);

    if (participants.size() != ids.size()) {
      throw new RuntimeException("Nenhum participante encontrado");
    }

    return participants;
  }

  public void addPoints(UUID id, Integer quantity) {

    Participant foundParticipant = participantRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Erro ao encontrar participante"));

    int currentPoints = foundParticipant.getPoints() != null
        ? foundParticipant.getPoints()
        : 0;

    foundParticipant.setPoints(currentPoints + quantity);

    participantRepository.save(foundParticipant);
  }

  public void addMedals(UUID id, Integer quantity) {

    Participant foundParticipant = participantRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Erro ao encontrar participante"));

    // TODO For a while it'll be just a Integer, but eventualy it can count medals
    // related with the participant, like just picking the medals.size
    int currentMedals = foundParticipant.getMedals() != null
        ? foundParticipant.getMedals()
        : 0;

    foundParticipant.setMedals(currentMedals + quantity);

    participantRepository.save(foundParticipant);
  }

}

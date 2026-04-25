package com.project.muttley.service;

import com.project.muttley.dto.CreateParticipantDTO;
import com.project.muttley.dto.ParticipantResponseDTO;
import com.project.muttley.dto.UpdateParticipantDTO;
import com.project.muttley.model.Participant;
import com.project.muttley.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;


    public ParticipantResponseDTO create(CreateParticipantDTO dto) {
        if (participantRepository.existsByCpf(dto.getCpf())) {
            // throw new RuntimeException("CPF já cadastrado: " + dto.getCpf());
        }
        // if (participantRepository.existsByEmail(dto.getEmail())) {
        //     throw new RuntimeException("Email já cadastrado: " + dto.getEmail());
        // }

        Participant participant = new Participant();
        participant.setName(dto.getName());
        participant.setEmail(dto.getEmail());
        participant.setCpf(dto.getCpf());
        participant.setLinkedin(dto.getLinkedin());
        participant.setGithub(dto.getGithub());

        return ParticipantResponseDTO.from(participantRepository.save(participant));
    }


    public Page<ParticipantResponseDTO> get(String cpf, String name, Pageable pageable) {
        Page<Participant> page;

        if (cpf != null && !cpf.isBlank()) {
            page = participantRepository.findByCpfContaining(cpf, pageable);
        } else if (name != null && !name.isBlank()) {
            page = participantRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            page = participantRepository.findAll(pageable);
        }

        return page.map(ParticipantResponseDTO::from);
    }


    public ParticipantResponseDTO getById(Long id) {
        Participant participant = participantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Participante não encontrado: id=" + id));
        return ParticipantResponseDTO.from(participant);
    }

    public ParticipantResponseDTO getByCpf(String cpf) {
        Participant participant = participantRepository.findByCpf(cpf)
            .orElseThrow(() -> new RuntimeException("Participante não encontrado: cpf=" + cpf));
        return ParticipantResponseDTO.from(participant);
    }

    public ParticipantResponseDTO update(Long id, UpdateParticipantDTO dto) {
        Participant participant = participantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Participante não encontrado: id=" + id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            participant.setName(dto.getName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            boolean emailTaken = participantRepository.existsByEmail(dto.getEmail())
                && !dto.getEmail().equalsIgnoreCase(participant.getEmail());
            if (emailTaken) {
                throw new RuntimeException("Email já cadastrado: " + dto.getEmail());
            }
            participant.setEmail(dto.getEmail());
        }

        if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
            boolean cpfTaken = participantRepository.existsByCpf(dto.getCpf())
                && !dto.getCpf().equals(participant.getCpf());
            if (cpfTaken) {
                throw new RuntimeException("CPF já cadastrado: " + dto.getCpf());
            }
            participant.setCpf(dto.getCpf());
        }

        if (dto.getLinkedin() != null) {
            participant.setLinkedin(dto.getLinkedin());
        }

        if (dto.getGithub() != null) {
            participant.setGithub(dto.getGithub());
        }

        return ParticipantResponseDTO.from(participantRepository.save(participant));
    }

    public void delete(Long id) {
        if (!participantRepository.existsById(id)) {
            throw new RuntimeException("Participante não encontrado: id=" + id);
        }
        participantRepository.deleteById(id);
    }
}
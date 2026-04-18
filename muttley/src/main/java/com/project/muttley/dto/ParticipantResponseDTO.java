package com.project.muttley.dto;

import com.project.muttley.model.Participant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String linkedin;
    private String github;

    public static ParticipantResponseDTO from(Participant p) {
        return new ParticipantResponseDTO(
            p.getId(),
            p.getName(),
            p.getEmail(),
            p.getCpf(),
            p.getLinkedin(),
            p.getGithub()
        );
    }
}
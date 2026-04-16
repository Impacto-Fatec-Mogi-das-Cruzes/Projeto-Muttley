package com.project.muttley;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.muttley.model.Event;
import com.project.muttley.model.EventParticipation;
import com.project.muttley.model.Participant;
import com.project.muttley.model.User;
import com.project.muttley.model.enums.EventStatus;
import com.project.muttley.model.enums.ParticipantType;
import com.project.muttley.model.enums.ParticipationStatus;
import com.project.muttley.model.enums.RoleInEvent;
import com.project.muttley.model.enums.UserRole;
import com.project.muttley.repository.EventParticipationRepository;
import com.project.muttley.repository.EventRepository;
import com.project.muttley.repository.MedalRepository;
import com.project.muttley.repository.ParticipantRepository;
import com.project.muttley.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@ActiveProfiles("test")
class ApiIntegrationTest {

    private MockMvc mockMvc;
    @Autowired private WebApplicationContext context;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired private UserRepository userRepository;
    @Autowired private ParticipantRepository participantRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private EventParticipationRepository participationRepository;
    @Autowired private MedalRepository medalRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private User adminUser;
    private Participant participant1;
    private Event event1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

        medalRepository.deleteAll();
        participationRepository.deleteAll();
        eventRepository.deleteAll();
        participantRepository.deleteAll();
        userRepository.deleteAll();

        adminUser = createUser("admin", "Admin", "admin@test.local", "admin123", UserRole.ADMIN);
        createUser("staff", "Staff", "staff@test.local", "staff123", UserRole.STAFF);

        participant1 = createParticipant("Participante Um", "staff@test.local", ParticipantType.INTERNAL);
        createParticipant("Participante Dois", "p2@test.local", ParticipantType.EXTERNAL);

        event1 = createEvent("Evento Base", adminUser);
    }

    @Test
    void deveBloquearCrudSemAutenticacao() throws Exception {
        mockMvc.perform(get("/api/admin/events"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void deveBloquearCrudParaStaffSemPermissaoAdmin() throws Exception {
        MockHttpSession staffSession = login("staff@test.local", "staff123");

        mockMvc.perform(get("/api/admin/events").session(staffSession))
                .andExpect(status().isForbidden());
    }

    @Test
    void deveExecutarCrudEventoComoAdmin() throws Exception {
        MockHttpSession adminSession = login("admin@test.local", "admin123");

        String created = mockMvc.perform(post("/api/admin/events")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "name", "Evento CRUD",
                                "description", "descricao",
                                "date", "2026-12-01",
                                "startTime", "19:00:00",
                                "endTime", "21:00:00",
                                "location", "Auditório",
                                "resourcesNeeded", "Projetor",
                                "status", "SCHEDULED",
                                "createdByUserId", adminUser.getId()
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Evento CRUD"))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(created).get("id").asLong();

        mockMvc.perform(get("/api/admin/events/" + id).session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(put("/api/admin/events/" + id)
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "name", "Evento Atualizado",
                                "description", "descricao2",
                                "date", "2026-12-02",
                                "startTime", "20:00:00",
                                "endTime", "22:00:00",
                                "location", "Lab",
                                "resourcesNeeded", "Som",
                                "status", "IN_PROGRESS",
                                "createdByUserId", adminUser.getId()
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Evento Atualizado"));

        mockMvc.perform(delete("/api/admin/events/" + id).session(adminSession))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveValidarPayloadInvalidoEvento() throws Exception {
        MockHttpSession adminSession = login("admin@test.local", "admin123");

        mockMvc.perform(post("/api/admin/events")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "name", "",
                                "date", "2026-12-01",
                                "startTime", "19:00:00",
                                "location", "",
                                "status", "DRAFT",
                                "createdByUserId", adminUser.getId()
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deveExecutarCrudParticipanteEParticipacao() throws Exception {
        MockHttpSession adminSession = login("admin@test.local", "admin123");

        String participantCreated = mockMvc.perform(post("/api/admin/participants")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "name", "Novo Participante",
                                "email", "novo.participante@test.local",
                                "ra", "9988",
                                "linkedinUrl", "",
                                "github", "",
                                "participantType", "INTERNAL"
                        ))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long participantId = objectMapper.readTree(participantCreated).get("id").asLong();

        String participationCreated = mockMvc.perform(post("/api/admin/event-participations")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "eventId", event1.getId(),
                                "participantId", participantId,
                                "roleInEvent", "SPEAKER",
                                "participationStatus", "REGISTERED",
                                "notes", "Observação"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleInEvent").value("SPEAKER"))
                .andReturn().getResponse().getContentAsString();

        Long participationId = objectMapper.readTree(participationCreated).get("id").asLong();

        mockMvc.perform(delete("/api/admin/event-participations/" + participationId).session(adminSession))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveExecutarCrudMedalha() throws Exception {
        MockHttpSession adminSession = login("admin@test.local", "admin123");

        String medalCreated = mockMvc.perform(post("/api/admin/medals")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "eventId", event1.getId(),
                                "name", "Medalha Teste",
                                "description", "desc",
                                "category", "categoria",
                                "score", 15,
                                "targetRole", "LISTENER",
                                "active", true
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Medalha Teste"))
                .andReturn().getResponse().getContentAsString();

        Long medalId = objectMapper.readTree(medalCreated).get("id").asLong();

        mockMvc.perform(put("/api/admin/medals/" + medalId)
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "eventId", event1.getId(),
                                "name", "Medalha Atualizada",
                                "description", "desc2",
                                "category", "cat2",
                                "score", 20,
                                "targetRole", "ORGANIZER",
                                "active", true
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.targetRole").value("ORGANIZER"));

        mockMvc.perform(delete("/api/admin/medals/" + medalId).session(adminSession))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveExporFluxoParticipanteAutenticado() throws Exception {
        MockHttpSession adminSession = login("admin@test.local", "admin123");

        EventParticipation participation = new EventParticipation();
        participation.setEvent(event1);
        participation.setParticipant(participant1);
        participation.setRoleInEvent(RoleInEvent.LISTENER);
        participation.setParticipationStatus(ParticipationStatus.PRESENT);
        participationRepository.save(participation);

        mockMvc.perform(post("/api/admin/medals")
                        .session(adminSession)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "eventId", event1.getId(),
                                "name", "Medalha Ouvinte",
                                "description", "desc",
                                "category", "cat",
                                "score", 11,
                                "targetRole", "LISTENER",
                                "active", true
                        ))))
                .andExpect(status().isCreated());

        MockHttpSession staffSession = login("staff@test.local", "staff123");

        mockMvc.perform(get("/api/user/dashboard").session(staffSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participant.email").value("staff@test.local"))
                .andExpect(jsonPath("$.participations[0].eventName").value("Evento Base"))
                .andExpect(jsonPath("$.medals[0].name").value("Medalha Ouvinte"));
    }

    private MockHttpSession login(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/userLogin")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email)
                        .param("senha", password))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(false);
    }

    private String json(Object payload) throws Exception {
        return objectMapper.writeValueAsString(payload);
    }

    private User createUser(String username, String name, String email, String rawPassword, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return userRepository.save(user);
    }

    private Participant createParticipant(String name, String email, ParticipantType type) {
        Participant participant = new Participant();
        participant.setName(name);
        participant.setEmail(email);
        participant.setParticipantType(type);
        return participantRepository.save(participant);
    }

    private Event createEvent(String name, User createdBy) {
        Event event = new Event();
        event.setName(name);
        event.setDescription("Evento de teste");
        event.setLocation("Sala A");
        event.setDate(LocalDate.of(2026, 10, 5));
        event.setStartTime(LocalTime.of(18, 0));
        event.setStatus(EventStatus.SCHEDULED);
        event.setCreatedBy(createdBy);
        return eventRepository.save(event);
    }
}

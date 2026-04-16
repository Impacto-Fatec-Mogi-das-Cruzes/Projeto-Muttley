// package com.project.muttley.config;

// import com.project.muttley.model.Event;
// import com.project.muttley.model.EventParticipation;
// import com.project.muttley.model.Medal;
// import com.project.muttley.model.Participant;
// import com.project.muttley.model.User;
// import com.project.muttley.model.enums.EventStatus;
// import com.project.muttley.model.enums.ParticipantType;
// import com.project.muttley.model.enums.ParticipationStatus;
// import com.project.muttley.model.enums.RoleInEvent;
// import com.project.muttley.model.enums.UserRole;
// import com.project.muttley.repository.EventParticipationRepository;
// import com.project.muttley.repository.EventRepository;
// import com.project.muttley.repository.MedalRepository;
// import com.project.muttley.repository.ParticipantRepository;
// import com.project.muttley.repository.UserRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;

// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.LocalTime;

// @Component
// public class DataSeeder implements CommandLineRunner {

//     private final UserRepository userRepository;
//     private final ParticipantRepository participantRepository;
//     private final EventRepository eventRepository;
//     private final EventParticipationRepository participationRepository;
//     private final MedalRepository medalRepository;
//     private final PasswordEncoder passwordEncoder;

//     public DataSeeder(
//             UserRepository userRepository,
//             ParticipantRepository participantRepository,
//             EventRepository eventRepository,
//             EventParticipationRepository participationRepository,
//             MedalRepository medalRepository,
//             PasswordEncoder passwordEncoder) {
//         this.userRepository = userRepository;
//         this.participantRepository = participantRepository;
//         this.eventRepository = eventRepository;
//         this.participationRepository = participationRepository;
//         this.medalRepository = medalRepository;
//         this.passwordEncoder = passwordEncoder;
//     }

//     @Override
//     public void run(String... args) {
//         if (userRepository.count() > 0 || eventRepository.count() > 0) {
//             return;
//         }

//         User admin = createUser("admin", "Administrador", "admin@muttley.local", "admin123", UserRole.ADMIN);
//         createUser("staff1", "Equipe Staff", "staff@muttley.local", "staff123", UserRole.STAFF);

//         Participant p1 = createParticipant("Ana Interna", "ana.interna@muttley.local", "12345", ParticipantType.INTERNAL);
//         Participant p2 = createParticipant("Bruno Interno", "bruno.interno@muttley.local", "54321", ParticipantType.INTERNAL);
//         Participant p3 = createParticipant("Carla Externa", "carla.externa@muttley.local", null, ParticipantType.EXTERNAL);

//         Event event1 = createEvent("Semana Tech 2026", "Evento principal de tecnologia", "Auditório Principal",
//                 LocalDate.now().plusDays(10), LocalTime.of(19, 0), EventStatus.SCHEDULED, admin);
//         Event event2 = createEvent("Workshop API", "Laboratório de integração", "Lab 3",
//                 LocalDate.now().plusDays(20), LocalTime.of(14, 0), EventStatus.DRAFT, admin);

//         createParticipation(event1, p1, RoleInEvent.LISTENER, ParticipationStatus.REGISTERED, null);
//         createParticipation(event1, p2, RoleInEvent.SPEAKER, ParticipationStatus.PRESENT, LocalDateTime.now());
//         createParticipation(event1, p3, RoleInEvent.ORGANIZER, ParticipationStatus.PRESENT, LocalDateTime.now());

//         createMedal(event1, "Medalha Ouvinte", "Reconhecimento para ouvintes", "participacao", 10, RoleInEvent.LISTENER);
//         createMedal(event1, "Medalha Palestrante", "Reconhecimento para palestrantes", "destaque", 20, RoleInEvent.SPEAKER);
//         createMedal(event1, "Medalha Organização", "Reconhecimento para equipe organizadora", "gestao", 25, RoleInEvent.ORGANIZER);
//     }

//     private User createUser(String username, String name, String email, String rawPassword, UserRole role) {
//         User user = new User();
//         user.setUsername(username);
//         user.setName(name);
//         user.setEmail(email);
//         user.setPassword(passwordEncoder.encode(rawPassword));
//         user.setRole(role);
//         return userRepository.save(user);
//     }

//     private Participant createParticipant(String name, String email, String ra, ParticipantType type) {
//         Participant participant = new Participant();
//         participant.setName(name);
//         participant.setEmail(email);
//         participant.setRa(ra);
//         participant.setParticipantType(type);
//         return participantRepository.save(participant);
//     }

//     private Event createEvent(
//             String name,
//             String description,
//             String location,
//             LocalDate date,
//             LocalTime startTime,
//             EventStatus status,
//             User createdBy) {
//         Event event = new Event();
//         event.setName(name);
//         event.setDescription(description);
//         event.setLocation(location);
//         event.setDate(date);
//         event.setStartTime(startTime);
//         event.setStatus(status);
//         event.setCreatedBy(createdBy);
//         return eventRepository.save(event);
//     }

//     private void createParticipation(
//             Event event,
//             Participant participant,
//             RoleInEvent role,
//             ParticipationStatus status,
//             LocalDateTime checkInAt) {
//         EventParticipation participation = new EventParticipation();
//         participation.setEvent(event);
//         participation.setParticipant(participant);
//         participation.setRoleInEvent(role);
//         participation.setParticipationStatus(status);
//         participation.setCheckInAt(checkInAt);
//         participationRepository.save(participation);
//     }

//     private void createMedal(
//             Event event,
//             String name,
//             String description,
//             String category,
//             Integer score,
//             RoleInEvent role) {
//         Medal medal = new Medal();
//         medal.setEvent(event);
//         medal.setName(name);
//         medal.setDescription(description);
//         medal.setCategory(category);
//         medal.setScore(score);
//         medal.setTargetRole(role);
//         medal.setActive(true);
//         medalRepository.save(medal);
//     }
// }

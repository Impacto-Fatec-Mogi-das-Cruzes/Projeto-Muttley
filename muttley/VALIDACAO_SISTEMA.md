# Validacao do Sistema Muttley

## Stack
- Spring Boot 4, Spring Security, Spring Data JPA, Thymeleaf.
- Banco principal: PostgreSQL.
- Testes: JUnit + MockMvc + H2.

## Endpoints Principais
- Autenticacao:
  - `GET /login`
  - `POST /userLogin`
  - `POST /logout`
  - `GET/POST /register`
- Admin (protegido por role ADMIN):
  - `GET/POST /api/admin/events`
  - `GET/PUT/DELETE /api/admin/events/{id}`
  - `GET/POST /api/admin/participants`
  - `GET/PUT/DELETE /api/admin/participants/{id}`
  - `GET/POST /api/admin/event-participations`
  - `GET/PUT/DELETE /api/admin/event-participations/{id}`
  - `GET/POST /api/admin/medals`
  - `GET/PUT/DELETE /api/admin/medals/{id}`
- Usuario/Participante:
  - `GET /api/user/dashboard`

## Credenciais Seed
- Admin: `admin@muttley.local` / `admin123`
- Staff: `staff@muttley.local` / `staff123`

## Frontend Minimo
- Admin: `GET /admin/dashboard`
  - Consulta de eventos, participantes, participacoes e medalhas.
  - Criacao de evento.
- Participante: `GET /user/dashboard`
  - Exibe participante, participacoes e medalhas relacionadas ao papel no evento.

## Como executar
1. Subir banco PostgreSQL conforme `application.properties`.
2. Executar backend:
   - `mvn spring-boot:run`
3. Executar testes:
   - `mvn test`

## O que foi validado
- Login por rota legitima com sessao.
- Bloqueio sem autenticacao em rota admin.
- Bloqueio de staff em rota admin (403).
- CRUD completo de:
  - Evento
  - Participante
  - EventParticipation
  - Medalha
- Validacao de payload invalido (400).
- Fluxo do participante em `GET /api/user/dashboard`.

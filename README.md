# Projeto-Muttley

Projeto da disciplina de Laboratório de Engenharia de Software da Faculdade de Tecnologia da Zona Leste (FATEC-ZL)
Gerenciador de eventos, criação e envio de certificados para participantes.

---

# Responsáveis

* João Paulo Akira Sigue
* Luciano Akihiro Tokuno
* Marcos Guilherme Tasato
* Natalia Quirino de Oliveira
---
# Como iniciar
Etapas de inicialização do Backend do projeto

## Requisitos
* Docker

## Inicialização

Clone o projeto com:
```terminal
  git clone https://github.com/Impacto-Fatec-Mogi-das-Cruzes/Projeto-Muttley
```

Crie o .env baseado no .env.example
```env

# PostgreSQL
POSTGRES_DB=Database
POSTGRES_USER=Username
POSTGRES_PASSWORD=Password
POSTGRES_PORT=Port

# JWT
JWT_SECRET=Secret
JWT_EXPIRATION=3600000

# AWS S3
AWS_S3_BUCKET=Bucket-name
AWS_S3_REGION=Bucket-region
AWS_ACCESS_KEY_ID=Access-key
AWS_SECRET_ACCESS_KEY=Secret-access-key
AWS_S3_ENDPOINT=Bucket-endpoint
AWS_S3_PUBLIC_BASE_URL=Bucket-public-url

# Email Service
MAIL_HOST=Host
MAIL_PORT=Port
MAIL_USERNAME=Username
MAIL_PASSWORD=Password
MAIL_FROM=From
MAIL_SMTP_AUTH=false
MAIL_TLS=false

# Frontend Presence
APP_PUBLIC_BASE_URL=Frontend-baseurl

# PDF Service
PDF_PUBLIC_BASE_URL=http://pdfservice:1336

```

Entre na pasta do projeto:
```terminal
  cd Projeto-Muttley
```

Inicie o docker compose do projeto (**DEV** ou **PROD**):

**DEV**
``` terminal
  docker compose -f docker-compose.dev.yml up -d
```

**PROD**
``` terminal
  docker compose -f docker-compose.yml up -d
```

 Acesse o frontend:

 ```url
  https://muttley.netlify.app
```

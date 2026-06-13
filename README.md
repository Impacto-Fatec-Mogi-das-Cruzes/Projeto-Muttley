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

# MySql
MYSQL_DB=DB
MYSQL_USER=User
MYSQL_PASSWORD=Password
MYSQL_ROOT_PASSWORD=Root_Password
MYSQL_PORT=Port

# JWT
JWT_SECRET=secure_key
JWT_EXPIRATION=3600000

# AWS S3
AWS_S3_BUCKET=Bucket_name
AWS_S3_REGION=Bucket_region
AWS_ACCESS_KEY_ID=Access_ID
AWS_SECRET_ACCESS_KEY=Secret_Access_Key
AWS_S3_ENDPOINT=EndPoint
AWS_S3_PUBLIC_BASE_URL=EndPoint

# Email Service
MAIL_HOST=Host
MAIL_PORT=Port
MAIL_USERNAME=Username
MAIL_PASSWORD=Password
MAIL_FROM=From
MAIL_SMTP_AUTH=false
MAIL_TLS=false

# Frontend Presence
APP_PUBLIC_BASE_URL=Base_Url

# CORS allowed origins (comma-separated, no quotes)
APP_CORS_ALLOWED_ORIGINS=Cors_Origins

# PDF Service
PDF_PUBLIC_BASE_URL=Url


VITE_SUPABASE_PROJECT_ID=ID
VITE_SUPABASE_PUBLISHABLE_KEY=Key
VITE_SUPABASE_URL=Url
VITE_BASE_API=Api_key/api

```

Entre na pasta do projeto:
```terminal
  cd Projeto-Muttley
```
Entre na pasta do muttley:
```terminal
  cd muttley
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
